package name.yumao.ffxiv.chn;

import name.yumao.ffxiv.chn.swing.ProcessPanel;
import name.yumao.ffxiv.chn.util.res.Config;


public class FFXIVPatchMain {
    public static void main(String[] args) {
        Config.setConfigResource("global.properties");
        new ProcessPanel();
    }

}
