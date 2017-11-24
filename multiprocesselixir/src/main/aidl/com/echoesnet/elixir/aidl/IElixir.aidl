// IElixirAidlInterface.aidl
package com.echoesnet.elixir.aidl;

import com.echoesnet.elixir.aidl.IElixirListener;
interface IElixir {

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);

    String obeyCommend(int aOwnerID, String aCmdType, String aCmd);

    String getVersionCode(String partOfName);

    String getCacheData(String cmd, int owner);

    String callback(String cmd, int owner);


    void registerListener(in IElixirListener listener);
}
