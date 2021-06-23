package name.yumao.ffxiv.chn.thread;

import name.yumao.ffxiv.chn.replace.RepackEXDF;
import name.yumao.ffxiv.chn.swing.PercentPanel;
import name.yumao.ffxiv.chn.swing.ProcessPanel;

import javax.swing.*;
import java.io.File;

public class RepackThread implements Runnable{

    private final String resourceFolder;
    private final ProcessPanel processPanel;

    public RepackThread(String resourceFolder, ProcessPanel processPanel){
        this.resourceFolder = resourceFolder;
        this.processPanel = processPanel;
    }
    @Override
    public void run() {
        try {
            processPanel.repackButton.setEnabled(false);
            PercentPanel percentPanel = new PercentPanel("打包");
            new RepackEXDF(resourceFolder + File.separator + "0a0000.win32.index",percentPanel).repack("merge");
            JOptionPane.showMessageDialog(null, "Success", "Success", JOptionPane.PLAIN_MESSAGE);
            percentPanel.dispose();
            processPanel.repackButton.setEnabled(true);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }
}
