# Generated automatically from Makefile.in by configure.
##########################################################################
# Makefile.in for Samba - rewritten for autoconf support
# Copyright Andrew Tridgell 1992-1998
###########################################################################

prefix=/usr/local/samba
exec_prefix=${prefix}
mandir=${prefix}/man
sysconfdir=${prefix}/etc

LIBS= -ldl -lnsl -lcrypt
LDAPLIBS=
CC=gcc
SHLD=${CC} 
CFLAGS=-O 
CPPFLAGS=-D_LARGEFILE64_SOURCE -D_FILE_OFFSET_BITS=64 -D_GNU_SOURCE 
LDFLAGS=
AWK=gawk
DYNEXP=-Wl,--export-dynamic

TERMLDFLAGS=
TERMLIBS=

LINK=$(CC) $(FLAGS) $(LDFLAGS)

INSTALLCMD=/usr/bin/install -c
INSTALLCLIENTCMD_SH=:
INSTALLCLIENTCMD_A=:

srcdir=.
builddir=/home/shuge/network_src/smb_source/samba-2.2.12/source
top_builddir=.
SHELL=/bin/sh

BASEDIR= /usr/local/samba
BINDIR = ${exec_prefix}/bin
DATADIR = ${prefix}/share
# sbindir is mapped to bindir when compiling SAMBA in 2.0.x compatibility mode.
SBINDIR = ${exec_prefix}/bin
LIBDIR = ${exec_prefix}/lib
VARDIR = ${prefix}/var
CONFIGDIR = $(LIBDIR)
MANDIR = ${prefix}/man
INCLUDEDIR = ${prefix}/include

# The permissions to give the executables
INSTALLPERMS = 0755

# set these to where to find various files
# These can be overridden by command line switches (see smbd(8))
# or in smb.conf (see smb.conf(5))
LOGFILEBASE = $(VARDIR)
CONFIGFILE = $(CONFIGDIR)/smb.conf
LMHOSTSFILE = $(CONFIGDIR)/lmhosts
DRIVERFILE = $(CONFIGDIR)/printers.def
PASSWD_PROGRAM = /usr/bin/passwd
# This is where smbpasswd et al go
PRIVATEDIR = ${prefix}/private

SMB_PASSWD_FILE = $(PRIVATEDIR)/smbpasswd
TDB_PASSWD_FILE = $(PRIVATEDIR)/smbpasswd.tdb

# This is where SWAT images and help files go
SWATDIR = ${prefix}/swat

# the directory where lock files go
LOCKDIR = ${VARDIR}/locks

# the directorty where pid files go
PIDDIR = $(VARDIR)/locks

# libsmbclient support here
BLDSHARED = true
LIBSMBCLIENT_MAJOR=0
LIBSMBCLIENT_MINOR=1

# The directory where code page definition files go
CODEPAGEDIR = $(LIBDIR)/codepages

# The current codepage definition list.
CODEPAGELIST= 437 737 775 850 852 861 932 866 949 950 936 1251 ISO8859-1 ISO8859-2 ISO8859-5 ISO8859-7 KOI8-R 857 ISO8859-9 \
			ISO8859-13 ISO8859-15 1125 KOI8-U

PASSWD_FLAGS = -DPASSWD_PROGRAM=\"$(PASSWD_PROGRAM)\" -DSMB_PASSWD_FILE=\"$(SMB_PASSWD_FILE)\" -DTDB_PASSWD_FILE=\"$(TDB_PASSWD_FILE)\"
FLAGS1 = $(CFLAGS) -I./popt -Iinclude -I$(srcdir)/include -I$(srcdir)/ubiqx -I$(srcdir)/smbwrapper $(CPPFLAGS) -DLOGFILEBASE=\"$(LOGFILEBASE)\"
FLAGS2 = -DCONFIGFILE=\"$(CONFIGFILE)\" -DLMHOSTSFILE=\"$(LMHOSTSFILE)\"  
FLAGS3 = -DSWATDIR=\"$(SWATDIR)\" -DSBINDIR=\"$(SBINDIR)\" -DLOCKDIR=\"$(LOCKDIR)\" -DCODEPAGEDIR=\"$(CODEPAGEDIR)\"
FLAGS4 = -DDRIVERFILE=\"$(DRIVERFILE)\" -DBINDIR=\"$(BINDIR)\" -DPIDDIR=\"$(PIDDIR)\" -DLIBDIR=\"$(LIBDIR)\"
FLAGS5 = $(FLAGS1) $(FLAGS2) $(FLAGS3) $(FLAGS4) -DHAVE_INCLUDES_H
FLAGS  = $(ISA) $(FLAGS5) $(PASSWD_FLAGS)
FLAGS32  = $(ISA32) $(FLAGS5) $(PASSWD_FLAGS)

WINBIND_PROGS = bin/wbinfo
WINBIND_SPROGS = bin/winbindd
WINBIND_PAM_PROGS = 
WINBIND_LPROGS = nsswitch/libnss_winbind.so

SPROGS = bin/smbd bin/nmbd bin/swat
PROGS1 = bin/smbclient bin/smbspool bin/testparm bin/testprns bin/smbstatus bin/smbcontrol bin/tdbbackup bin/make_printerdef  
PROGS2 = bin/smbpasswd bin/make_smbcodepage bin/rpcclient bin/make_unicodemap bin/smbcacls      
MPROGS = 
LPROGS = $(WINBIND_PAM_PROGS) $(WINBIND_LPROGS)
PROGS = $(PROGS1) $(PROGS2) $(MPROGS) bin/nmblookup
TORTURE_PROGS = bin/smbtorture bin/msgtest bin/masktest bin/locktest bin/locktest2
SHLIBS = 

SCRIPTS = $(srcdir)/script/smbtar  $(srcdir)/script/findsmb

QUOTAOBJS=smbd/noquotas.o

######################################################################
# object file lists
######################################################################

TDBBASE_OBJ = tdb/tdb.o tdb/spinlock.o
TDB_OBJ = $(TDBBASE_OBJ) tdb/tdbutil.o 

LIB_OBJ = lib/charcnv.o lib/charset.o lib/debug.o lib/fault.o \
          lib/getsmbpass.o lib/interface.o lib/kanji.o lib/md4.o \
          lib/interfaces.o lib/pidfile.o lib/replace.o \
          lib/signal.o lib/system.o lib/sendfile.o lib/time.o \
	  lib/ufc.o lib/genrand.o lib/username.o lib/util_getent.o lib/access.o lib/smbrun.o \
	  lib/bitmap.o lib/crc32.o lib/snprintf.o lib/wins_srv.o \
	  lib/util_str.o lib/util_sid.o \
	  lib/util_unistr.o lib/util_file.o \
	  lib/util.o lib/util_sock.o lib/util_sec.o smbd/ssl.o \
	  lib/talloc.o lib/hash.o lib/substitute.o lib/fsusage.o \
	  lib/ms_fnmatch.o lib/select.o lib/error.o lib/messages.o \
	  lib/pam_errors.o nsswitch/wb_client.o nsswitch/wb_common.o $(TDB_OBJ)

READLINE_OBJ = lib/readline.o

UBIQX_OBJ = ubiqx/ubi_BinTree.o ubiqx/ubi_Cache.o ubiqx/ubi_SplayTree.o \
            ubiqx/ubi_dLinkList.o ubiqx/ubi_sLinkList.o ubiqx/debugparse.o

PARAM_OBJ = param/loadparm.o param/params.o 

LIBSMB_OBJ = libsmb/clientgen.o libsmb/cliconnect.o libsmb/clifile.o \
		libsmb/clirap.o libsmb/clierror.o libsmb/climessage.o \
		libsmb/clireadwrite.o libsmb/clilist.o libsmb/cliprint.o \
		libsmb/clitrans.o libsmb/clisecdesc.o libsmb/clidgram.o \
		libsmb/namequery.o libsmb/nmblib.o libsmb/clistr.o \
		libsmb/nterr.o libsmb/smbdes.o libsmb/smbencrypt.o \
		libsmb/smberr.o libsmb/credentials.o libsmb/pwd_cache.o \
		libsmb/clioplock.o libsmb/errormap.o libsmb/doserr.o \
		libsmb/passchange.o libsmb/unexpected.o $(RPC_PARSE_OBJ1) \
		libsmb/namecache.o

LIBMSRPC_OBJ = libsmb/cli_lsarpc.o libsmb/cli_samr.o libsmb/cli_spoolss.o \
				libsmb/cli_netlogon.o libsmb/cli_srvsvc.o libsmb/cli_dfs.o \
				libsmb/cli_reg.o \
				rpc_client/cli_pipe.o libsmb/cli_pipe_util.o

RPC_SERVER_OBJ = rpc_server/srv_lsa.o rpc_server/srv_lsa_nt.o \
                 rpc_server/srv_lsa_hnd.o rpc_server/srv_netlog.o rpc_server/srv_netlog_nt.o \
                 rpc_server/srv_pipe_hnd.o rpc_server/srv_reg.o rpc_server/srv_reg_nt.o \
                 rpc_server/srv_samr.o rpc_server/srv_samr_nt.o rpc_server/srv_srvsvc.o rpc_server/srv_srvsvc_nt.o \
                 rpc_server/srv_util.o rpc_server/srv_wkssvc.o rpc_server/srv_wkssvc_nt.o \
                 rpc_server/srv_pipe.o rpc_server/srv_dfs.o rpc_server/srv_dfs_nt.o \
		 rpc_server/srv_spoolss.o rpc_server/srv_spoolss_nt.o rpc_client/cli_spoolss_notify.o

# this includes only the low level parse code, not stuff
# that requires knowledge of security contexts
RPC_PARSE_OBJ1 = rpc_parse/parse_prs.o rpc_parse/parse_sec.o \
				rpc_parse/parse_misc.o

RPC_PARSE_OBJ = rpc_parse/parse_lsa.o rpc_parse/parse_net.o \
                rpc_parse/parse_reg.o rpc_parse/parse_rpc.o \
                rpc_parse/parse_samr.o rpc_parse/parse_srv.o \
                rpc_parse/parse_wks.o \
		rpc_parse/parse_spoolss.o rpc_parse/parse_dfs.o

RPC_CLIENT_OBJ = rpc_client/cli_netlogon.o rpc_client/cli_pipe.o \
		rpc_client/cli_login.o \
		rpc_client/cli_trust.o

LOCKING_OBJ = locking/locking.o locking/brlock.o locking/posix.o

PASSDB_OBJ = passdb/passdb.o passdb/secrets.o \
	     passdb/pass_check.o passdb/smbpassfile.o \
	     passdb/machine_sid.o passdb/pdb_smbpasswd.o \
	     passdb/pampass.o passdb/pdb_tdb.o passdb/pdb_ldap.o \
	     passdb/pdb_nisplus.o

PROFILE_OBJ = profile/profile.o

OPLOCK_OBJ = smbd/oplock.o smbd/oplock_irix.o smbd/oplock_linux.o

NOTIFY_OBJ = smbd/notify.o smbd/notify_hash.o smbd/notify_kernel.o

SMBD_OBJ1 = smbd/server.o smbd/files.o smbd/chgpasswd.o smbd/connection.o \
	    smbd/utmp.o smbd/session.o \
            smbd/dfree.o smbd/dir.o smbd/password.o smbd/conn.o smbd/fileio.o \
            smbd/ipc.o smbd/lanman.o smbd/mangle.o smbd/mangle_hash2.o smbd/mangle_hash.o \
	    smbd/mangle_map.o smbd/negprot.o smbd/message.o smbd/nttrans.o smbd/pipes.o \
            smbd/reply.o smbd/trans2.o smbd/uid.o \
	    smbd/dosmode.o smbd/filename.o smbd/open.o smbd/close.o \
	    smbd/blocking.o smbd/sec_ctx.o \
	    smbd/vfs.o smbd/vfs-wrap.o smbd/statcache.o \
            smbd/posix_acls.o lib/sysacls.o \
	    smbd/process.o smbd/service.o smbd/error.o \
	    printing/printfsp.o lib/util_seaccess.o \
	    libsmb/cli_pipe_util.o

PRINTING_OBJ = printing/pcap.o printing/print_svid.o \
				 printing/print_cups.o printing/print_generic.o \
				printing/lpq_parse.o printing/load.o

PRINTBACKEND_OBJ = printing/printing.o printing/nt_printing.o

MSDFS_OBJ = msdfs/msdfs.o 

SMBD_OBJ = $(SMBD_OBJ1) $(MSDFS_OBJ) $(PARAM_OBJ) $(LIBSMB_OBJ) $(UBIQX_OBJ) \
           $(RPC_SERVER_OBJ) $(RPC_PARSE_OBJ) $(RPC_CLIENT_OBJ) \
           $(LOCKING_OBJ) $(PASSDB_OBJ) $(PRINTING_OBJ) $(PROFILE_OBJ) $(LIB_OBJ) \
	   $(PRINTBACKEND_OBJ) $(QUOTAOBJS) $(OPLOCK_OBJ) $(NOTIFY_OBJ)


NMBD_OBJ1 = nmbd/asyncdns.o nmbd/nmbd.o nmbd/nmbd_become_dmb.o \
            nmbd/nmbd_become_lmb.o nmbd/nmbd_browserdb.o \
            nmbd/nmbd_browsesync.o nmbd/nmbd_elections.o \
            nmbd/nmbd_incomingdgrams.o nmbd/nmbd_incomingrequests.o \
            nmbd/nmbd_lmhosts.o nmbd/nmbd_logonnames.o nmbd/nmbd_mynames.o \
            nmbd/nmbd_namelistdb.o nmbd/nmbd_namequery.o \
            nmbd/nmbd_nameregister.o nmbd/nmbd_namerelease.o \
            nmbd/nmbd_nodestatus.o nmbd/nmbd_packets.o \
            nmbd/nmbd_processlogon.o nmbd/nmbd_responserecordsdb.o \
            nmbd/nmbd_sendannounce.o nmbd/nmbd_serverlistdb.o \
            nmbd/nmbd_subnetdb.o nmbd/nmbd_winsproxy.o nmbd/nmbd_winsserver.o \
            nmbd/nmbd_workgroupdb.o nmbd/nmbd_synclists.o

NMBD_OBJ = $(NMBD_OBJ1) $(PARAM_OBJ) $(LIBSMB_OBJ) $(UBIQX_OBJ) \
           $(PROFILE_OBJ) $(LIB_OBJ)

SWAT_OBJ = web/cgi.o web/diagnose.o web/startstop.o web/statuspage.o \
           web/swat.o $(PRINTING_OBJ) $(LIBSMB_OBJ) $(LOCKING_OBJ) \
           $(PARAM_OBJ) $(PASSDB_OBJ) $(RPC_PARSE_OBJ) \
           $(UBIQX_OBJ) $(LIB_OBJ) 

SMBSH_OBJ = smbwrapper/smbsh.o smbwrapper/shared.o \
            $(PARAM_OBJ) $(UBIQX_OBJ) $(LIB_OBJ)

MAKE_SMBCODEPAGE_OBJ = utils/make_smbcodepage.o $(PARAM_OBJ) \
                       $(UBIQX_OBJ) $(LIB_OBJ)

MAKE_UNICODEMAP_OBJ = utils/make_unicodemap.o $(PARAM_OBJ) \
                       $(UBIQX_OBJ) $(LIB_OBJ)

MAKE_PRINTERDEF_OBJ = utils/make_printerdef.o $(PARAM_OBJ) \
                      $(UBIQX_OBJ) $(LIB_OBJ)

STATUS_OBJ = utils/status.o $(LOCKING_OBJ) $(PARAM_OBJ) \
             $(UBIQX_OBJ) $(PROFILE_OBJ) $(LIB_OBJ)

SMBCONTROL_OBJ = utils/smbcontrol.o $(LOCKING_OBJ) $(PARAM_OBJ) \
             $(UBIQX_OBJ) $(PROFILE_OBJ) $(LIB_OBJ)

TESTPARM_OBJ = utils/testparm.o \
               $(PARAM_OBJ) $(UBIQX_OBJ) $(LIB_OBJ)

TESTPRNS_OBJ = utils/testprns.o $(PARAM_OBJ) $(PRINTING_OBJ) $(UBIQX_OBJ) \
               $(LIB_OBJ)

SMBPASSWD_OBJ = utils/smbpasswd.o $(PARAM_OBJ) \
				$(LIBSMB_OBJ) $(PASSDB_OBJ) $(GROUPDB_OBJ)\
				$(UBIQX_OBJ) $(RPC_CLIENT_OBJ) $(RPC_PARSE_OBJ) $(LIB_OBJ) \
				libsmb/cli_lsarpc.o libsmb/cli_samr.o libsmb/cli_pipe_util.o

PDBEDIT_OBJ = utils/pdbedit.o $(PARAM_OBJ) $(LIBSMB_OBJ) $(PASSDB_OBJ) \
		$(UBIQX_OBJ) $(LIB_OBJ) $(GROUPDB_OBJ)

RPCCLIENT_OBJ1 = rpcclient/rpcclient.o rpcclient/cmd_lsarpc.o \
				rpcclient/cmd_samr.o rpcclient/cmd_spoolss.o \
				rpcclient/cmd_netlogon.o rpcclient/cmd_srvsvc.o \
				rpcclient/cmd_dfs.o rpcclient/cmd_reg.o \
				rpc_client/cli_login.o rpc_client/cli_netlogon.o \
				rpcclient/display_sec.o
 
RPCCLIENT_OBJ = $(RPCCLIENT_OBJ1) \
				$(PARAM_OBJ) $(LIBSMB_OBJ) $(UBIQX_OBJ) $(LIB_OBJ) \
				$(RPC_PARSE_OBJ) $(PASSDB_OBJ) $(LIBMSRPC_OBJ) \
				$(READLINE_OBJ)

PAM_WINBIND_OBJ = nsswitch/pam_winbind.po nsswitch/wb_common.po lib/snprintf.po

SMBW_OBJ = smbwrapper/smbw.o \
		smbwrapper/smbw_dir.o smbwrapper/smbw_stat.o \
		smbwrapper/realcalls.o smbwrapper/shared.o \
		$(LIBSMB_OBJ) $(PARAM_OBJ) \
                $(UBIQX_OBJ) $(LIB_OBJ)

SMBWRAPPER_OBJ = $(SMBW_OBJ) smbwrapper/wrapped.o

LIBSMBCLIENT_OBJ = libsmb/libsmbclient.o $(LIB_OBJ) $(LIBSMB_OBJ) \
		$(PARAM_OBJ) $(UBIQX_OBJ)

CLIENT_OBJ = client/client.o client/clitar.o \
             $(PARAM_OBJ) $(LIBSMB_OBJ) $(UBIQX_OBJ) $(LIB_OBJ) \
             $(READLINE_OBJ)

CUPS_OBJ = client/smbspool.o $(PARAM_OBJ) $(LIBSMB_OBJ) $(UBIQX_OBJ) $(LIB_OBJ)

MOUNT_OBJ = client/smbmount.o \
             $(PARAM_OBJ) $(LIBSMB_OBJ) $(UBIQX_OBJ) $(LIB_OBJ)

MNT_OBJ = client/smbmnt.o \
             $(PARAM_OBJ) $(LIBSMB_OBJ) $(UBIQX_OBJ) $(LIB_OBJ)

UMOUNT_OBJ = client/smbumount.o \
             $(PARAM_OBJ) $(LIBSMB_OBJ) $(UBIQX_OBJ) $(LIB_OBJ)

NMBLOOKUP_OBJ = utils/nmblookup.o $(PARAM_OBJ) $(UBIQX_OBJ) \
                $(LIBSMB_OBJ) $(LIB_OBJ)

SMBTORTURE_OBJ = utils/torture.o utils/nbio.o $(LIBSMB_OBJ) $(PARAM_OBJ) \
                 $(UBIQX_OBJ) $(LIB_OBJ)

MASKTEST_OBJ = utils/masktest.o $(LIBSMB_OBJ) $(PARAM_OBJ) \
                 $(UBIQX_OBJ) $(LIB_OBJ)

MSGTEST_OBJ = utils/msgtest.o $(LIBSMB_OBJ) $(PARAM_OBJ) \
                 $(UBIQX_OBJ) $(LIB_OBJ)

LOCKTEST_OBJ = utils/locktest.o $(LOCKING_OBJ) $(LIBSMB_OBJ) $(PARAM_OBJ) \
                 $(UBIQX_OBJ) $(LIB_OBJ)

NSSTEST_OBJ = utils/nsstest.o $(LIBSMB_OBJ) $(PARAM_OBJ) \
				$(UBIQX_OBJ) $(LIB_OBJ)

SMBCACLS_OBJ = utils/smbcacls.o $(LOCKING_OBJ) $(LIBSMB_OBJ) $(PARAM_OBJ) \
				$(UBIQX_OBJ) $(LIB_OBJ) $(RPC_PARSE_OBJ) $(PASSDB_OBJ) \
				$(LIBMSRPC_OBJ)


LOCKTEST2_OBJ = utils/locktest2.o $(LOCKING_OBJ) $(LIBSMB_OBJ) $(PARAM_OBJ) \
                 $(UBIQX_OBJ) $(LIB_OBJ)

RPCTORTURE_OBJ = utils/rpctorture.o \
             rpcclient/display.o \
             rpcclient/cmd_lsarpc.o \
             rpcclient/cmd_wkssvc.o \
             rpcclient/cmd_samr.o \
             rpcclient/cmd_srvsvc.o \
             rpcclient/cmd_netlogon.o \
             $(PARAM_OBJ) $(LIBSMB_OBJ) $(UBIQX_OBJ) $(LIB_OBJ) \
             $(RPC_PARSE_OBJ) $(PASSDB_OBJ)

RPCCHECK_OBJ = utils/rpccheck.o \
		$(LIB_OBJ)  $(RPC_PARSE_OBJ) \
		$(UBIQX_OBJ) $(PARAM_OBJ) $(LIBSMB_OBJ)

DEBUG2HTML_OBJ = utils/debug2html.o ubiqx/debugparse.o

SMBFILTER_OBJ = utils/smbfilter.o $(LIBSMB_OBJ) $(PARAM_OBJ) \
                 $(UBIQX_OBJ) $(LIB_OBJ)

PROTO_OBJ = $(SMBD_OBJ) $(NMBD_OBJ) $(SWAT_OBJ) $(CLIENT_OBJ) \
	    $(SMBWRAPPER_OBJ) $(SMBTORTURE_OBJ) $(RPCCLIENT_OBJ1) \
	    $(RPC_CLIENT_OBJ) $(LIBMSRPC_OBJ)

NSS_OBJ_0 = nsswitch/wins.o $(PARAM_OBJ) $(UBIQX_OBJ) $(LIBSMB_OBJ) $(LIB_OBJ)
NSS_OBJ = $(NSS_OBJ_0:.o=.po)

PICOBJS = $(SMBWRAPPER_OBJ:.o=.po)
PICOBJS32 = $(SMBWRAPPER_OBJ:.o=.po32)

PAM_SMBPASS_OBJ_0 = pam_smbpass/pam_smb_auth.o pam_smbpass/pam_smb_passwd.o \
		pam_smbpass/pam_smb_acct.o pam_smbpass/support.o \
		lib/debug.o lib/util_sid.o lib/messages.o lib/util_str.o \
		lib/wins_srv.o lib/substitute.o lib/select.o lib/util.o \
		nsswitch/wb_client.o nsswitch/wb_common.o lib/system.o \
		lib/charset.o lib/util_file.o lib/kanji.o lib/genrand.o \
		lib/username.o lib/util_getent.o lib/charcnv.o lib/time.o lib/md4.o \
		lib/util_unistr.o lib/signal.o lib/talloc.o lib/ms_fnmatch.o \
		lib/util_sock.o lib/smbrun.o lib/util_sec.o lib/snprintf.o \
		ubiqx/ubi_sLinkList.o libsmb/smbencrypt.o libsmb/smbdes.o \
		smbd/ssl.o lib/access.o \
		lib/interfaces.o $(PARAM_OBJ) $(TDB_OBJ) $(PASSDB_OBJ)

PAM_SMBPASS_OBJ = $(PAM_SMBPASS_OBJ_0:.o=.po)
LIBSMBCLIENT_PICOBJS = $(LIBSMBCLIENT_OBJ:.o=.po)

WINBINDD_OBJ1 = \
		nsswitch/winbindd.o       \
		nsswitch/winbindd_user.o  \
		nsswitch/winbindd_group.o \
		nsswitch/winbindd_idmap.o \
		nsswitch/winbindd_util.o  \
		nsswitch/winbindd_cache.o \
		nsswitch/winbindd_pam.o   \
		nsswitch/winbindd_sid.o   \
		nsswitch/winbindd_misc.o  \
		nsswitch/winbindd_cm.o \
		nsswitch/winbindd_wins.o \
		nsswitch/winbindd_rpc.o

NECESSARY_BECAUSE_SAMBA_DEPENDENCIES_ARE_SO_BROKEN_OBJ = \
		smbd/password.o smbd/utmp.o smbd/session.o smbd/uid.o smbd/sec_ctx.o \
		rpc_client/cli_netlogon.o rpc_client/cli_login.o \
		smbd/chgpasswd.o

WINBINDD_OBJ = \
		$(WINBINDD_OBJ1) $(NOPROTO_OBJ) $(PASSDB_OBJ) \
		$(PARAM_OBJ) $(UBIQX_OBJ) $(LIB_OBJ) \
		$(LIBSMB_OBJ) $(LIBMSRPC_OBJ) $(RPC_PARSE_OBJ) \
		$(GROUPDB_OBJ) $(PROFILE_OBJ) \
		$(NECESSARY_BECAUSE_SAMBA_DEPENDENCIES_ARE_SO_BROKEN_OBJ)

WBINFO_OBJ = nsswitch/wbinfo.o libsmb/smbencrypt.o libsmb/smbdes.o \
	passdb/secrets.o

WINBIND_NSS_OBJ = nsswitch/winbind_nss.o nsswitch/wb_common.o 

WINBIND_NSS_PICOBJS = $(WINBIND_NSS_OBJ:.o=.po)

POPT_OBJS=popt/findme.o popt/popt.o popt/poptconfig.o \
	popt/popthelp.o popt/poptparse.o

TDBBACKUP_OBJ = tdb/tdbbackup.o $(TDBBASE_OBJ)

TDBDUMP_OBJ =  tdb/tdbdump.o $(TDBBASE_OBJ)

######################################################################
# now the rules...
######################################################################

all : CHECK $(SPROGS) $(PROGS) $(WINBIND_PROGS) $(WINBIND_SPROGS) $(LPROGS)

# The following "everything" is NOT needed except by Samba developers - so do not use this!
everything : CHECK $(SPROGS) $(PROGS) $(SHLIBS) nsswitch smbwrapper smbtorture debug2html smbfilter

pam_smbpass : CHECK bin/pam_smbpass.so

smbwrapper : CHECK   

libsmbclient : CHECK bin/libsmbclient.a 

torture : CHECK $(TORTURE_PROGS)

smbtorture : CHECK bin/smbtorture

masktest : CHECK bin/masktest

msgtest : CHECK bin/msgtest

locktest : CHECK bin/locktest

smbcacls : CHECK bin/smbcacls

locktest2 : CHECK bin/locktest2

rpctorture : CHECK bin/rpctorture

bin/rpccheck: $(RPCCHECK_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(RPCCHECK_OBJ) $(LDFLAGS) $(LIBS)

debug2html : CHECK bin/debug2html

smbfilter : CHECK bin/smbfilter

nsswitch : CHECK $(WINBIND_PROGS) $(WINBIND_SPROGS) $(LPROGS)

wins : CHECK nsswitch/libnss_wins.so

.SUFFIXES:
.SUFFIXES: .c .o .po .po32 .lo

CHECK:
	@echo "Using FLAGS = $(FLAGS)"
	@echo "Using FLAGS32 = $(FLAGS32)"
	@echo "Using LIBS = $(LIBS)"

MAKEDIR = || exec false; \
	  if test -d "$$dir"; then :; else \
	  echo mkdir "$$dir"; \
	  mkdir -p "$$dir" >/dev/null 2>&1 || \
	  test -d "$$dir" || \
	  mkdir "$$dir" || \
	  exec false; fi || exec false

.c.o: 
	@if (: >> $@ || : > $@) >/dev/null 2>&1; then rm -f $@; else \
	 dir=`echo $@ | sed 's,/[^/]*$$,,;s,^$$,.,'` $(MAKEDIR); fi
	@echo Compiling $*.c
	@$(CC) -I. -I$(srcdir) $(FLAGS) -c $< \
	  -o $@ 
#	-mv `echo $@ | sed 's%^.*/%%g'` $@

.c.po: 
	@if (: >> $@ || : > $@) >/dev/null 2>&1; then rm -f $@; else \
	  dir=`echo $@ | sed 's,/[^/]*$$,,;s,^$$,.,'` $(MAKEDIR); fi
	@echo Compiling $*.c with -fPIC
	@$(CC) -I. -I$(srcdir) $(FLAGS) -fPIC -c $< \
	  -o $*.po.o 
#	-mv `echo $@ | sed -e 's%^.*/%%g' -e 's%\.po$$%.o%'` $@.o
	@mv $*.po.o $@

# this is for IRIX
.c.po32: 
	@if (: >> $@ || : > $@) >/dev/null 2>&1; then rm -f $@; else \
	  dir=`echo $@ | sed 's,/[^/]*$$,,;s,^$$,.,'` $(MAKEDIR); fi
	@echo Compiling $*.c with -fPIC and -32
	@$(CC) -32 -I. -I$(srcdir) $(FLAGS32) -fPIC -c $< \
	  -o $*.po32.o 
#	-mv `echo $@ | sed -e 's%^.*/%%g' -e 's%\.po32$$%.o%'` $@.o
	@mv $*.po32.o $@

bin/.dummy:
	@if (: >> $@ || : > $@) >/dev/null 2>&1; then :; else \
	  dir=bin $(MAKEDIR); fi
	@: >> $@ || : > $@ # what a fancy emoticon!

bin/smbd: $(SMBD_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(SMBD_OBJ) $(LDFLAGS) $(DYNEXP) $(LIBS) $(LDAPLIBS)

bin/nmbd: $(NMBD_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(NMBD_OBJ) $(LDFLAGS) $(LIBS)

bin/swat: $(SWAT_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(SWAT_OBJ) $(LDFLAGS) $(DYNEXP) $(LIBS) $(LDAPLIBS) 

bin/rpcclient: $(RPCCLIENT_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(RPCCLIENT_OBJ) $(LDFLAGS) $(DYNEXP) $(TERMLDFLAGS) $(TERMLIBS) $(LIBS) $(LDAPLIBS)

bin/smbclient: $(CLIENT_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(CLIENT_OBJ) $(LDFLAGS) $(TERMLDFLAGS) $(TERMLIBS) $(LIBS)

bin/smbspool: $(CUPS_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(CUPS_OBJ) $(LDFLAGS) $(LIBS)

bin/smbmount: $(MOUNT_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(MOUNT_OBJ) $(LDFLAGS) $(LIBS)

bin/smbmnt: $(MNT_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(MNT_OBJ) $(LDFLAGS) $(LIBS)

bin/smbumount: $(UMOUNT_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(UMOUNT_OBJ) $(LDFLAGS) $(LIBS)

bin/testparm: $(TESTPARM_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(TESTPARM_OBJ) $(LDFLAGS) $(LIBS)

bin/testprns: $(TESTPRNS_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(TESTPRNS_OBJ) $(LDFLAGS) $(LIBS)

bin/smbstatus: $(STATUS_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(STATUS_OBJ) $(LDFLAGS) $(LIBS)

bin/smbcontrol: $(SMBCONTROL_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(SMBCONTROL_OBJ) $(LDFLAGS) $(LIBS)

bin/smbpasswd: $(SMBPASSWD_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(SMBPASSWD_OBJ) $(LDFLAGS) $(DYNEXP) $(LIBS) $(LDAPLIBS)

bin/pdbedit: $(PDBEDIT_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(PDBEDIT_OBJ) $(LDFLAGS) $(DYNEXP) $(LIBS) $(LDAPLIBS)

bin/make_smbcodepage: $(MAKE_SMBCODEPAGE_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(MAKE_SMBCODEPAGE_OBJ) $(LDFLAGS) $(LIBS)

bin/make_unicodemap: $(MAKE_UNICODEMAP_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(MAKE_UNICODEMAP_OBJ) $(LDFLAGS) $(LIBS)

bin/nmblookup: $(NMBLOOKUP_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(NMBLOOKUP_OBJ) $(LDFLAGS) $(LIBS)

bin/make_printerdef: $(MAKE_PRINTERDEF_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(MAKE_PRINTERDEF_OBJ) $(LDFLAGS) $(LIBS)

bin/smbtorture: $(SMBTORTURE_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(SMBTORTURE_OBJ) $(LDFLAGS) $(LIBS)

bin/masktest: $(MASKTEST_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(MASKTEST_OBJ) $(LDFLAGS) $(LIBS)

bin/msgtest: $(MSGTEST_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(MSGTEST_OBJ) $(LDFLAGS) $(LIBS)

bin/smbcacls: $(SMBCACLS_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(SMBCACLS_OBJ) $(LDFLAGS) $(DYNEXP) $(LIBS) $(LDAPLIBS)

bin/locktest: $(LOCKTEST_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(LOCKTEST_OBJ) $(LDFLAGS) $(LIBS)

bin/locktest2: $(LOCKTEST2_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(LOCKTEST2_OBJ) $(LDFLAGS) $(LIBS)

bin/nsstest: $(NSSTEST_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(NSSTEST_OBJ) $(LDFLAGS) $(LIBS)

bin/rpctorture: $(RPCTORTURE_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(RPCTORTURE_OBJ) $(LDFLAGS) $(DYNEXP) $(LIBS)

bin/debug2html: $(DEBUG2HTML_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(DEBUG2HTML_OBJ) $(LDFLAGS) $(LIBS)

bin/smbfilter: $(SMBFILTER_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(SMBFILTER_OBJ) $(LDFLAGS) $(LIBS)


bin/smbw_sample: $(SMBW_OBJ) utils/smbw_sample.o bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(SMBW_OBJ) utils/smbw_sample.o $(LDFLAGS) $(LIBS)

bin/smbsh: $(SMBSH_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(SMBSH_OBJ) $(LDFLAGS) $(LIBS)

bin/smbwrapper.so: $(PICOBJS) bin/.dummy
	@echo Linking shared library $@
	@$(SHLD) -shared -o $@ $(PICOBJS) $(LDFLAGS) $(LIBS) \
		-Wl,-soname=`basename $@`

bin/smbwrapper.32.so: $(PICOBJS32) bin/.dummy
	@echo Linking shared library $@
	@$(SHLD) -32 -shared -o $@ $(PICOBJS32) $(LDFLAGS) $(LIBS) \
		-Wl,-soname=`basename $@`

bin/libsmbclient.so: $(LIBSMBCLIENT_PICOBJS) bin/.dummy
	@echo Linking libsmbclient shared library $@
	@$(SHLD) -shared -o $@ $(LIBSMBCLIENT_PICOBJS) $(LDFLAGS) $(LIBS) \
		-Wl,-soname=`basename $@`.$(LIBSMBCLIENT_MAJOR)

bin/libsmbclient.a: $(LIBSMBCLIENT_PICOBJS) bin/.dummy
	@echo Linking libsmbclient non-shared library $@
	@-$(AR) -rc $@ $(LIBSMBCLIENT_PICOBJS)

bin/pam_smbpass.so: $(PAM_SMBPASS_OBJ) bin/.dummy
	@echo Linking shared library $@
	$(SHLD) -shared -o $@ $(PAM_SMBPASS_OBJ) $(LDFLAGS) -lpam $(DYNEXP) $(LIBS) $(LDAPLIBS) -lc \
		-Wl,-soname=`basename $@`

nsswitch/libnss_wins.so: $(NSS_OBJ)
	@echo "Linking $@"
	@$(SHLD) -shared -o $@ $(NSS_OBJ) -lc \
		-Wl,-soname=`basename $@`

bin/winbindd: $(WINBINDD_OBJ) bin/.dummy
	@echo Linking $@
	@$(LINK) -o $@ $(WINBINDD_OBJ) $(DYNEXP) $(LIBS) $(LDAPLIBS)

nsswitch/libns_winbind.so: $(WINBIND_NSS_PICOBJS)
	@echo "Linking $@"
	@$(SHLD) -shared -o $@ $(WINBIND_NSS_PICOBJS)  \
		-Wl,-soname=`basename $@`

nsswitch/libnss_winbind.so: $(WINBIND_NSS_PICOBJS)
	@echo "Linking $@"
	@$(SHLD) -shared -o $@ $(WINBIND_NSS_PICOBJS)  \
		-Wl,-soname=`basename $@`

nsswitch/pam_winbind.so: $(PAM_WINBIND_OBJ)
	@echo Linking $@
	@$(SHLD) -shared -o $@ $(PAM_WINBIND_OBJ) \
		-Wl,-soname=`basename $@`

bin/wbinfo: $(WBINFO_OBJ) $(PARAM_OBJ) $(LIB_OBJ) $(NOPROTO_OBJ) \
			$(UBIQX_OBJ) $(POPT_OBJS) bin/.dummy
	@echo Linking $@
	@$(LINK) -o $@ $(WBINFO_OBJ) $(PARAM_OBJ) $(LIB_OBJ) $(NOPROTO_OBJ) \
		$(UBIQX_OBJ) $(LIBS) $(POPT_OBJS)

bin/tdbbackup: $(TDBBACKUP_OBJ) bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(TDBBACKUP_OBJ)

bin/tdbdump: $(TDBDUMP_OBJ)  bin/.dummy
	@echo Linking $@
	@$(CC) $(FLAGS) -o $@ $(TDBDUMP_OBJ)

install: installbin installman installscripts installcp installswat

installdirs:
	$(SHELL) $(srcdir)/install-sh -d -m $(INSTALLPERMS) $(BASEDIR)
	$(SHELL) $(srcdir)/install-sh -d -m $(INSTALLPERMS) $(SBINDIR)
	$(SHELL) $(srcdir)/install-sh -d -m $(INSTALLPERMS) $(BINDIR)
	$(SHELL) $(srcdir)/install-sh -d -m $(INSTALLPERMS) $(LIBDIR)
	$(SHELL) $(srcdir)/install-sh -d -m $(INSTALLPERMS) $(VARDIR)
	$(SHELL) $(srcdir)/install-sh -d -m $(INSTALLPERMS) $(PIDDIR)
	$(SHELL) $(srcdir)/install-sh -d -m $(INSTALLPERMS) $(CODEPAGEDIR)

installservers: all installdirs
	@$(SHELL) $(srcdir)/script/installbin.sh $(INSTALLPERMS) $(BASEDIR) $(SBINDIR) $(LIBDIR) $(VARDIR) $(SPROGS)

installbin: all installdirs
	@$(SHELL) $(srcdir)/script/installbin.sh $(INSTALLPERMS) $(BASEDIR) $(SBINDIR) $(LIBDIR) $(VARDIR) $(SPROGS) $(WINBIND_SPROGS)
	@$(SHELL) $(srcdir)/script/installbin.sh $(INSTALLPERMS) $(BASEDIR) $(BINDIR) $(LIBDIR) $(VARDIR) $(PROGS) $(WINBIND_PROGS)

installscripts: installdirs
	@$(SHELL) $(srcdir)/script/installscripts.sh $(INSTALLPERMS) $(BINDIR) $(SCRIPTS)

installcp: installdirs installbin
	@$(SHELL) $(srcdir)/script/installcp.sh $(srcdir) $(LIBDIR) $(CODEPAGEDIR) $(BINDIR) $(CODEPAGELIST)

installswat: installdirs
	@$(SHELL) $(srcdir)/script/installswat.sh $(SWATDIR) $(srcdir)

installclientlib:
	-$(INSTALLCLIENTCMD_SH) bin/libsmbclient.so
	-$(INSTALLCLIENTCMD_A) bin/libsmbclient.a
	-$(INSTALLCMD) -d ${prefix}/include
	-$(INSTALLCMD) include/libsmbclient.h ${prefix}/include

# revert to the previously installed version
revert:
	@$(SHELL) $(srcdir)/script/revert.sh $(SBINDIR) $(SPROGS) ${WINBIND_SPROGS}
	@$(SHELL) $(srcdir)/script/revert.sh $(BINDIR) $(PROGS) $(SCRIPTS) ${WINBIND_PROGS}

installman:
	@$(SHELL) $(srcdir)/script/installman.sh $(MANDIR) $(srcdir) ""

uninstall: uninstallman uninstallbin uninstallscripts uninstallcp

uninstallman:
	@$(SHELL) $(srcdir)/script/uninstallman.sh $(MANDIR) $(srcdir)

uninstallbin:
	@$(SHELL) $(srcdir)/script/uninstallbin.sh $(INSTALLPERMS) $(BASEDIR) $(SBINDIR) $(LIBDIR) $(VARDIR) $(SPROGS) $(WINBIND_SPROGS)
	@$(SHELL) $(srcdir)/script/uninstallbin.sh $(INSTALLPERMS) $(BASEDIR) $(BINDIR) $(LIBDIR) $(VARDIR) $(PROGS) $(WINBIND_PROGS)

uninstallscripts:
	@$(SHELL) $(srcdir)/script/uninstallscripts.sh $(INSTALLPERMS) $(BINDIR) $(SCRIPTS)

uninstallcp:
	@$(SHELL) $(srcdir)/script/uninstallcp.sh $(CODEPAGEDIR) $(CODEPAGELIST)

clean: 
	-rm -f core */*~ *~ */*.o */*.po */*.po32 */*.so* */*.a

winbindd_proto:
	@cd $(srcdir) && $(SHELL) script/mkproto.sh $(AWK) \
		-h _WINBINDD_PROTO_H_ nsswitch/winbindd_proto.h \
		$(WINBINDD_OBJ1)

proto: 
	@echo rebuilding include/proto.h
	@cd $(srcdir) && $(AWK) -f script/mkproto.awk `echo $(PROTO_OBJ) | tr ' ' '\n' | sed -e 's/\.o/\.c/g' | sort -u | egrep -v 'ubiqx/|wrapped'` > include/proto.h

etags:
	etags `find $(srcdir) -name "*.[ch]" | grep -v /CVS/`

ctags:
	ctags `find $(srcdir) -name "*.[ch]" | grep -v /CVS/`

realclean: clean
	-rm -f config.log $(PROGS) $(SPROGS) $(WINBIND_PROGS) $(WINBIND_SPROGS) $(LPROGS) bin/.dummy
	-rm -f bin/*
	-rmdir bin

distclean: realclean
	-rm -f include/config.h Makefile
	-rm -f config.status config.cache so_locations
	-rm -rf .deps

# this target is really just for my use. It only works on a limited
# range of machines and is used to produce a list of potentially
# dead (ie. unused) functions in the code. (tridge)
finddead:
	nm */*.o |grep 'U ' | awk '{print $$2}' | sort -u > nmused.txt
	nm */*.o |grep 'T ' | awk '{print $$3}' | sort -u > nmfns.txt
	comm -13 nmused.txt nmfns.txt 


# when configure.in is updated, reconfigure
$(srcdir)/configure: $(srcdir)/configure.in
	@echo "WARNING: you need to rerun autoconf"

config.status: $(srcdir)/configure
	@echo "WARNING: you need to run configure"

Makefile: $(srcdir)/Makefile.in config.status
	@echo "WARNING: you need to run ./config.status"
