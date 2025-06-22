package name.yumao.ffxiv.chn.thread;

import name.yumao.ffxiv.chn.replace.ReplaceFont;
import name.yumao.ffxiv.chn.swing.PercentPanel;
import name.yumao.ffxiv.chn.swing.ProcessPanel;

import javax.swing.*;
import java.io.File;

public class FontPatchThread implements Runnable{

    private final String resourceName;
    private final String resourceFolder;
    private final String outputFolder;
    private final ProcessPanel processPanel;

    public FontPatchThread(String resourceName, ProcessPanel processPanel){
        this.resourceName = resourceName;
        this.resourceFolder = new File("resource" + File.separator + resourceName.toLowerCase()).getAbsolutePath();
        this.outputFolder = new File("output" + File.separator + resourceName + File.separator + "000000.win32.index").getAbsolutePath();
        this.processPanel = processPanel;
    }
    @Override
    public void run() {
        try {
            processPanel.disableAllButtons();
            PercentPanel percentPanel = new PercentPanel("字型補完計畫 - " + resourceName);

            new ReplaceFont(outputFolder, resourceFolder, percentPanel).replace();
            
            JOptionPane.showMessageDialog(null, resourceName, "字型補完計畫 - 成功", JOptionPane.PLAIN_MESSAGE);
            percentPanel.dispose();
            processPanel.enableAllButtons();
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "字型補完計畫 - 失敗", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }
}
