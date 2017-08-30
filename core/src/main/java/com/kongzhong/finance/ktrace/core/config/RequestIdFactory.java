package com.kongzhong.finance.ktrace.core.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by IFT8 on 17/4/3.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestIdFactory {
    private static final int SEED = getLocalMAC().hashCode();
    private static final Random RANDOM = new Random();

    /**
     * 生成RequestID（随机Id）
     */
    public static String generateRequestId() {
        return String.valueOf(getNextRandom());
    }

    /**
     * 随机数(RANDOM.nextLong ^ MAC.hash ^ currentThreadId)
     */
    private static long getNextRandom() {
        return RANDOM.nextLong() ^ SEED ^ Thread.currentThread().getId();
    }

    /**
     * 本机MAC地址
     */
    private static String getLocalMAC() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface byInetAddress = NetworkInterface.getByInetAddress(localHost);
            byte[] hardwareAddress = byInetAddress.getHardwareAddress();
            return Arrays.toString(hardwareAddress);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }
}
