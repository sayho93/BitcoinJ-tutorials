package org.bitcoinj.examples;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.Wallet;

import java.io.File;

public class MyHDWallet {

    public NetworkParameters getTestNetParam() {
        return MainNetParams.fromID(MainNetParams.ID_TESTNET);
    }

    public WalletAppKit initialWallet(NetworkParameters params) {
        String filePrefix = "wallet-service-testnet";
        return new WalletAppKit(params, new File("."), filePrefix);
    }




}
