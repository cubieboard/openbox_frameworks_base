/* 
   Unix SMB/CIFS implementation.
   low level tdb backup and restore utility
   Copyright (C) Andrew Tridgell              2002

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
*/

/*

  This program is meant for backup/restore of tdb databases. Typical usage would be:
     tdbbackup *.tdb
  when Samba shuts down cleanly, which will make a backup of all the local databases
  to *.bak files. Then on Samba startup you would use:
     tdbbackup -v *.tdb
  and this will check the databases for corruption and if corruption is detected then
  the backup will be restored.

  You may also like to do a backup on a regular basis while Samba is
  running, perhaps using cron.

  The reason this program is needed is to cope with power failures
  while Samba is running. A power failure could lead to database
  corruption and Samba will then not start correctly.

  Note that many of the databases in Samba are transient and thus
  don't need to be backed up, so you can optimise the above a little
  by only running the backup on the critical databases.

 */

#include <errno.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <fcntl.h>
#include <time.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/time.h>
#include <ctype.h>
#include <signal.h>
#include "tdb.h"
#include "version.h"

static int failed;

static char *add_suffix(const char *name, const char *suffix)
{
	char *ret;
	int len = strlen(name) + strlen(suffix) + 1;
	ret = malloc(len);
	if (!ret) {
		fprintf(stderr,"Out of memory!\n");
		exit(1);
	}
	strncpy(ret, name, len);
	strncat(ret, suffix, len);
	return ret;
}

static int copy_fn(TDB_CONTEXT *tdb, TDB_DATA key, TDB_DATA dbuf, void *state)
{
	TDB_CONTEXT *tdb_new = (TDB_CONTEXT *)state;

	if (tdb_store(tdb_new, key, dbuf, TDB_INSERT) != 0) {
		fprintf(stderr,"Failed to insert into %s\n", tdb_new->name);
		failed = 1;
		return 1;
	}
	return 0;
}


static int test_fn(TDB_CONTEXT *tdb, TDB_DATA key, TDB_DATA dbuf, void *state)
{
	return 0;
}

/*
  carefully backup a tdb, validating the contents and
  only doing the backup if its OK
  this function is also used for restore
*/
static int backup_tdb(const char *old_name, const char *new_name)
{
	TDB_CONTEXT *tdb;
	TDB_CONTEXT *tdb_new;
	char *tmp_name;
	struct stat st;
	int count1, count2;

	tmp_name = add_suffix(new_name, ".tmp");

	/* stat the old tdb to find its permissions */
	if (stat(old_name, &st) != 0) {
		perror(old_name);
		return 1;
	}

	/* open the old tdb */
	tdb = tdb_open(old_name, 0, 0, O_RDWR, 0);
	if (!tdb) {
		printf("Failed to open %s\n", old_name);
		return 1;
	}

	/* create the new tdb */
	unlink(tmp_name);
	tdb_new = tdb_open(tmp_name, tdb->header.hash_size, 
			   TDB_DEFAULT, O_RDWR|O_CREAT|O_EXCL, 
			   st.st_mode & 0777);
	if (!tdb_new) {
		perror(tmp_name);
		free(tmp_name);
		return 1;
	}

	/* lock the old tdb */
	if (tdb_lockall(tdb) != 0) {
		fprintf(stderr,"Failed to lock %s\n", old_name);
		tdb_close(tdb);
		tdb_close(tdb_new);
		unlink(tmp_name);
		free(tmp_name);
		return 1;
	}

	failed = 0;

	/* traverse and copy */
	count1 = tdb_traverse(tdb, copy_fn, (void *)tdb_new);
	if (count1 < 0 || failed) {
		fprintf(stderr,"failed to copy %s\n", old_name);
		tdb_close(tdb);
		tdb_close(tdb_new);
		unlink(tmp_name);
		free(tmp_name);
		return 1;
	}

	/* close the old tdb */
	tdb_close(tdb);

	/* close the new tdb and re-open read-only */
	tdb_close(tdb_new);
	tdb_new = tdb_open(tmp_name, 0, TDB_DEFAULT, O_RDONLY, 0);
	if (!tdb_new) {
		fprintf(stderr,"failed to reopen %s\n", tmp_name);
		unlink(tmp_name);
		perror(tmp_name);
		free(tmp_name);
		return 1;
	}
	
	/* traverse the new tdb to confirm */
	count2 = tdb_traverse(tdb_new, test_fn, 0);
	if (count2 != count1) {
		fprintf(stderr,"failed to copy %s\n", old_name);
		tdb_close(tdb_new);
		unlink(tmp_name);
		free(tmp_name);
		return 1;
	}

	/* make sure the new tdb has reached stable storage */
	fsync(tdb_new->fd);

	/* close the new tdb and rename it to .bak */
	tdb_close(tdb_new);
	unlink(new_name);
	if (rename(tmp_name, new_name) != 0) {
		perror(new_name);
		free(tmp_name);
		return 1;
	}

	printf("%s : %d records\n", old_name, count1);
	free(tmp_name);

	return 0;
}



/*
  verify a tdb and if it is corrupt then restore from *.bak
*/
static int verify_tdb(const char *fname, const char *bak_name)
{
	TDB_CONTEXT *tdb;
	int count = -1;

	/* open the tdb */
	tdb = tdb_open(fname, 0, 0, O_RDONLY, 0);

	/* traverse the tdb, then close it */
	if (tdb) {
		count = tdb_traverse(tdb, test_fn, NULL);
		tdb_close(tdb);
	}

	/* count is < 0 means an error */
	if (count < 0) {
		printf("restoring %s\n", fname);
		return backup_tdb(bak_name, fname);
	}

	printf("%s : %d records\n", fname, count);

	return 0;
}


static void usage(void)
{
	printf("Usage: tdbbackup [options] <fname...>\n\n");
	printf("Version: %s\n", VERSION);
	printf("   -h            this help message\n");
	printf("   -s suffix     set the backup suffix\n");
	printf("   -v            veryify mode (restore if corrupt)\n");
}
		

 int main(int argc, char *argv[])
{
	int i;
	int ret = 0;
	int c;
	int verify = 0;
	const char *suffix = ".bak";
	extern int optind;
	extern char *optarg;

	while ((c = getopt(argc, argv, "vhs:")) != -1) {
		switch (c) {
		case 'h':
			usage();
			exit(0);
		case 'v':
			verify = 1;
			break;
		case 's':
			suffix = optarg;
			break;
		}
	}

	argc -= optind;
	argv += optind;

	if (argc < 1) {
		usage();
		exit(1);
	}

	for (i=0; i<argc; i++) {
		const char *fname = argv[i];
		char *bak_name;

		bak_name = add_suffix(fname, suffix);

		if (verify) {
			if (verify_tdb(fname, bak_name) != 0) {
				ret = 1;
			}
		} else {
			if (backup_tdb(fname, bak_name) != 0) {
				ret = 1;
			}
		}

		free(bak_name);
	}

	return ret;
}