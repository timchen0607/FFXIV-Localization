package name.yumao.ffxiv.chn.thread;

import name.yumao.ffxiv.chn.replace.ReplaceFont;
import name.yumao.ffxiv.chn.swing.PercentPanel;
import name.yumao.ffxiv.chn.swing.ProcessPanel;

import javax.swing.*;
import java.io.File;

public class PatchThread implements Runnable{

    private final String inputFolder;
    private final ProcessPanel processPanel;

    public PatchThread(String resourceFolder, ProcessPanel processPanel){
        this.inputFolder = resourceFolder;
        this.processPanel = processPanel;
    }
    @Override
    public void run() {
        try {
            processPanel.unpackButton.setEnabled(false);
            PercentPanel percentPanel = new PercentPanel("字體補丁");
            //字體補丁
            new ReplaceFont(inputFolder + File.separator + "000000.win32.index", "resource" + File.separator + "font").replace();
            JOptionPane.showMessageDialog(null, "Success", "Success", JOptionPane.PLAIN_MESSAGE);
            percentPanel.dispose();
            processPanel.repackButton.setEnabled(true);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }
}
