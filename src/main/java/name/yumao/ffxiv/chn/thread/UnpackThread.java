package name.yumao.ffxiv.chn.thread;

import java.io.File;

import javax.swing.JOptionPane;

import name.yumao.ffxiv.chn.replace.UnpackEXDF;
import name.yumao.ffxiv.chn.swing.PercentPanel;
import name.yumao.ffxiv.chn.swing.ProcessPanel;
import name.yumao.ffxiv.chn.util.res.Config;

public class UnpackThread implements Runnable{

    private final String inputFolder;
    private final ProcessPanel processPanel;
    private final String lang;

    public UnpackThread(String inputFolder, ProcessPanel processPanel){
        this.inputFolder = inputFolder  + File.separator + "0a0000.win32.index";
        this.processPanel = processPanel;
        this.lang = Config.getProperty("Language");
    }
    @Override
    public void run() {
        try {
            processPanel.unpackButton.setEnabled(false);
            PercentPanel percentPanel = new PercentPanel("拆包中 - " + lang);

            new UnpackEXDF(inputFolder, percentPanel).unpack();

            JOptionPane.showMessageDialog(null, lang, "拆包完成", JOptionPane.PLAIN_MESSAGE);
            percentPanel.dispose();
            processPanel.unpackButton.setEnabled(true);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "拆包失敗", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }
}
