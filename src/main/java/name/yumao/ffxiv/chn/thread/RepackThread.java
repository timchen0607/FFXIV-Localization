package name.yumao.ffxiv.chn.thread;

import java.io.File;

import javax.swing.JOptionPane;

import name.yumao.ffxiv.chn.replace.RepackEXDF;
import name.yumao.ffxiv.chn.swing.PercentPanel;
import name.yumao.ffxiv.chn.swing.ProcessPanel;
import name.yumao.ffxiv.chn.util.res.Config;

public class RepackThread implements Runnable {

    private final String outputFolder;
    private final ProcessPanel processPanel;
    private final String lang;

    public RepackThread(String outputFolder, ProcessPanel processPanel) {
        this.outputFolder = outputFolder + File.separator + "0a0000.win32.index";
        this.processPanel = processPanel;
        this.lang = Config.getProperty("Language");
    }

    @Override
    public void run() {
        try {
            processPanel.disableAllButtons();
            PercentPanel percentPanel = new PercentPanel("打包中 - " + lang);

            new RepackEXDF(outputFolder, percentPanel).repack();

            JOptionPane.showMessageDialog(null, lang, "打包完成", JOptionPane.PLAIN_MESSAGE);
            percentPanel.dispose();
            processPanel.enableAllButtons();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "打包失敗", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }
}
