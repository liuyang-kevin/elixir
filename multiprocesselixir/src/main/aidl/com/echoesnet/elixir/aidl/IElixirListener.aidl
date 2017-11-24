// IElixirListener.aidl
package com.echoesnet.elixir.aidl;
interface IElixirListener {
    void onElixirChange(String cmd, int id, String result);
}
