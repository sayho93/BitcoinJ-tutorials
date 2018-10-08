package org.bitcoinj.examples;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyHDWallet {

    public NetworkParameters getTestNetParam() {
        return MainNetParams.fromID(MainNetParams.ID_TESTNET);
    }

    public WalletAppKit initialWallet(NetworkParameters params) {
        String filePrefix = "wallet-service-testnet";
        return new WalletAppKit(params, new File("."), filePrefix);
    }

    public void synchBlockchain(WalletAppKit kit) {
        System.out.println("Synchronizing the blockchain ...");
        kit.startAsync();
        kit.awaitRunning();
        System.out.println("Synchronized the blockchain ...");
    }

    class MyWallet {
        String address = "";
        String balance = "";
        String seed = "";
        String creationTime = "";
        String mnemonics = "";

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getSeed() {
            return seed;
        }

        public void setSeed(String seed) {
            this.seed = seed;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(String creationTime) {
            this.creationTime = creationTime;
        }

        public String getMnemonics() {
            return mnemonics;
        }

        public void setMnemonics(String mnemonics) {
            this.mnemonics = mnemonics;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Address : \t" + address);
            sb.append("\r\n");
            sb.append("Balance : \t" + balance);
            sb.append("\r\n");
            sb.append("Seed : \t" + seed);
            sb.append("\r\n");
            sb.append("Creationtime : \t" + creationTime);
            sb.append("\r\n");
            sb.append("Mnemonics : \t" + mnemonics);
            sb.append("\r\n");
            return sb.toString();
        }
    }

    public List< MyWallet> getWalletAddress(WalletAppKit kit) {
        List< MyWallet> walletList = new ArrayList< MyWallet>();
        List<Address> list = kit.wallet().getWatchedAddresses();
        if (list.size() < 5) {
            kit.wallet().addWatchedAddress(kit.wallet().freshReceiveAddress());
            System.out.println("New address created");
        }
        MyWallet w = null;
        for (Address addr : kit.wallet().getWatchedAddresses()) {
            w = new MyWallet();
            w.setAddress(addr.toString());
            w.setBalance(kit.wallet().getBalance().toFriendlyString());
            DeterministicSeed seed = kit.wallet().getActiveKeyChain().getSeed();
            w.setSeed(seed.toHexString());
            if ( (seed.getCreationTimeSeconds()-1525190911)<1) {
                seed.setCreationTimeSeconds(System.currentTimeMillis());
            }
            w.setCreationTime(Utils.dateTimeFormat(seed.getCreationTimeSeconds()));
            w.setMnemonics(String.join("", seed.getMnemonicCode()));
            walletList.add(w);
        }
        return walletList;
    }

    public static void main(String[] args) {
        try {
            MyHDWallet myWallet = new MyHDWallet();
            WalletAppKit kit = myWallet.initialWallet(myWallet.getTestNetParam());
            myWallet.synchBlockchain(kit);
            List< MyWallet> walletList = myWallet.getWalletAddress(kit);
            for (MyWallet w : walletList) {
                System.out.println(w.toString());
            }
            Thread.sleep(1000);
            kit.stopAsync();
            kit.awaitTerminated();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}


