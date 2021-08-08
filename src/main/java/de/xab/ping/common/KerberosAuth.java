package de.xab.ping.common;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class KerberosAuth {
    private static final Logger logger = LoggerFactory.getLogger("PING");

    public static UserGroupInformation getUgi(String keytabPath, String cPrincipal, String krb5Path) throws IOException {
        if (keytabPath == null || cPrincipal == null) {
            return null;
        }
        pathPreCheck(keytabPath);
        File keytabFile = FileUtils.getFile(keytabPath);
        Configuration conf = new Configuration();
        if (krb5Path != null) {
            System.setProperty("java.security.krb5.conf", krb5Path);
        }
        conf.setBoolean("hadoop.security.authorization", true);
        conf.set("hadoop.security.authentication", "kerberos");
        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation userGroupInformation = UserGroupInformation.loginUserFromKeytabAndReturnUGI(cPrincipal, keytabFile.getAbsolutePath());
        logger.info(UserGroupInformation.getCurrentUser() + "------" + UserGroupInformation.getLoginUser());
        return userGroupInformation;
    }

    private static void pathPreCheck(String path) {
        File file = FileUtils.getFile(path);
        if (!file.exists()) {
            logger.error("file not found");
        }
    }
}
