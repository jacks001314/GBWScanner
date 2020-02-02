package com.gbw.scanner.plugins.bruteforce;

import com.gbw.scanner.plugins.bruteforce.ftp.GBWBruteForceFTPConfig;
import com.gbw.scanner.plugins.bruteforce.mail.GBWBruteForceMailConfig;
import com.gbw.scanner.plugins.bruteforce.mssql.GBWBruteForceMSSQLConfig;
import com.gbw.scanner.plugins.bruteforce.mysql.GBWBruteForceMySQLConfig;
import com.gbw.scanner.plugins.bruteforce.redis.GBWBruteForceRedisConfig;
import com.gbw.scanner.plugins.bruteforce.ssh.GBWBruteForceSSHConfig;

public class GBWBruteForceConfig {

    private int threads;

    private GBWBruteForceFTPConfig ftpConfig;
    private GBWBruteForceMailConfig mailConfig;
    private GBWBruteForceMySQLConfig mySQLConfig;
    private GBWBruteForceMSSQLConfig mssqlConfig;
    private GBWBruteForceRedisConfig redisConfig;
    private GBWBruteForceSSHConfig sshConfig;

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public GBWBruteForceFTPConfig getFtpConfig() {
        return ftpConfig;
    }

    public void setFtpConfig(GBWBruteForceFTPConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    public GBWBruteForceMailConfig getMailConfig() {
        return mailConfig;
    }

    public void setMailConfig(GBWBruteForceMailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    public GBWBruteForceMySQLConfig getMySQLConfig() {
        return mySQLConfig;
    }

    public void setMySQLConfig(GBWBruteForceMySQLConfig mySQLConfig) {
        this.mySQLConfig = mySQLConfig;
    }

    public GBWBruteForceMSSQLConfig getMssqlConfig() {
        return mssqlConfig;
    }

    public void setMssqlConfig(GBWBruteForceMSSQLConfig mssqlConfig) {
        this.mssqlConfig = mssqlConfig;
    }

    public GBWBruteForceRedisConfig getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(GBWBruteForceRedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    public GBWBruteForceSSHConfig getSshConfig() {
        return sshConfig;
    }

    public void setSshConfig(GBWBruteForceSSHConfig sshConfig) {
        this.sshConfig = sshConfig;
    }
}
