package com.quantumcoinwallet.app.keystorage;

import android.content.Context;
import android.security.keystore.UserNotAuthenticatedException;

import com.quantumcoinwallet.app.entity.KeyServiceException;

import java.security.InvalidKeyException;

public interface IKeyStore {

    boolean EncryptData(Context context, String address, String password, String keyPair);

    byte[] DecryptData(Context context, String address, String password) throws InvalidKeyException, KeyServiceException;

    //String ExportKey(Context context, String address);

    //byte[] ImportKey(Context context, String jsonString, String password);

    //void DeleteKey(Context context, String address);

}
