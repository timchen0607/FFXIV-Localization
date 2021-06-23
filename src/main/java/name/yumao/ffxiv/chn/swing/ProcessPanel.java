package name.yumao.ffxiv.chn.swing;

import name.yumao.ffxiv.chn.model.Language;
import name.yumao.ffxiv.chn.thread.UnpackThread;
import name.yumao.ffxiv.chn.thread.RepackThread;
import name.yumao.ffxiv.chn.thread.PatchThread;
import name.yumao.ffxiv.chn.util.HexUtils;
import name.yumao.ffxiv.chn.util.res.Config;
import name.yumao.ffxiv.chn.util.FileUtil;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;


public class ProcessPanel extends JFrame implements ActionListener {

    private static final Point origin = new Point();
    private static final String title = "FFXIV中文化";

    private final JLabel title_lable = new JLabel(title);
    private final Dimension dimension;

    private final JButton closeButton = new JButton("x");
    private final JPanel titlePanel = new JPanel();
    private final JPanel bodyPanel = new JPanel();

    public JButton unpackButton = new JButton("拆包");
    public JButton repackButton = new JButton("打包");
    public JButton fontPatchButton = new JButton("字體補丁");

    private final JLabel langLable = new JLabel("處理語言");
    private final JComboBox<String> langLableVal;

    private final JLabel inputPathLable = new JLabel("來源路徑");
    private final JTextField inputPathField = new JTextField(Config.getProperty("GamePath"));
    private final JButton inputPathButton = new JButton(".");
    private final JLabel comparePathLable = new JLabel("比較路徑");
    private final JTextField comparePathField = new JTextField(Config.getProperty("GameComparePath"));
    private final JButton comparePathButton = new JButton(".");

    public ProcessPanel() {
        super(title);
        setUndecorated(true);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        setIconImage(toolkit.createImage(HexUtils.hexStringToBytes("89504E470D0A1A0A0000000D4948445200000040000000400806000000AA6971DE000004EC4944415478DAED5B69485451141EB50DB3C56AA821A329C7596CD459A2FA57467B513F0209F147CB0F2B82C20ACA928ACA3F2544605909051651D962641B914542942456602B6DE492AD56A29665DF9137F27ACD7BB3F4EECCBB830F0EF7CD9B33EFDEEFDC73CE3DF79C3B3A5D082EB7DB3D283535D5EE703816B85CAE5CA7D3791C7407F74FF1DD1BB4F5681BD0D6815E821EE1FB4AB487D1AE453B2B2525C58A77F4D7F174198DC67E003D07200A00A20AD40EA09D81127EF71BEF20C194A05D6AB3D90C5AC6DD0B839C8FC19E007D0F06B01F02F905BA857EB2939393E334811A031982016DC6C0DEB2002D47E8F32BFA2C84798C090B70D8663C06918F417C535261B27750332B41E0DD3F4007ED76FBA850618F06F015E8FC83C2C07E92C3831F28C1FD975068044D04FA5B47A6C80C39A46C232FAD34E3684FA5A5A5CD236F1F4A9310994635CCC2A23A78805B056A5300FF009D4F062D24FB0C0778B1366012B254016E32990602D04985CEDAA17A5B70DF1B7C79821674869B842574DB7F81872A25E045F7153A790E1E1758A3D0D93E2D00F76212FB697C0183870A8DA3684DE1C56514E9D1CBA10107B4085E34D68280C163769B141C1DBD309A786929D432780F619256FAABF6168ACFE5C0E345EB3DBC00BF4C2B36EF874F68C378C72B82B75AAD43C9AE159C4A8E484B9C78DECA037811861AD93881BC38186E2AA8D00E116F2C789FF104DEA72928D932BE3B26F6A4F8BC9B47F002BD008498BFC0432A53F045875C6445DB5B11AF438E97235398290E74FAE2C11319E616D8BA5912115EE619BC4005E2D9CF5590D41AA9A6F0E2F57D68C0450FA0C1F8F05986B15A6A2B78561E01B34F02B8D195AFC3CD2605C674C9EC275146264204709A6C792A25226518AE7B592576450278C1B1E791000AE518E0F86648F047853AE5C5729748A13E09A05686E9B174F7841F4C8894D907EE339EE5EC938C7A6CF0A2FE791102BEBE3BB5EE2D9949EAE12DCB8AE7D722007C1D1569C4B3FAD10BE37B3C9F6DB7DB1345D15F14CBAC6E889C5E39300D97CE6A8D2F67016A9111142FF490CA72729B9F9C48716CD2F09D7298A0698AA9302A6C60862F45C2B246C517002EA28C744085548BC532003FBAC01160AA02D5824A29130D9AFB8F6D0773619DCF10CAD2E1DCE874600C8D68A9BE705528916FA74228804E27C74C491BA6352F746444C74B4045E8AC02ED6B7FD35E42E5B6992246A1CE7F97964F9A2DDC1753124528A25281258BCAE76827510E12CFF59E24AB262F2A4593AA2188180DB089341B142F4073469ACDE6617ABD3E2EA8DC7BCFD573755F06832196340C9A3591D66E98C57298C8D6AE8D0BAF179DEF0198C594592230A03D007604748E9212E4F0845D66AB42167723B70250A36A04019DE75900875488E31BB9150066AF4C8D1801ABD0082E0580C1DF5343005E3254DC6840934A51E26AEEC053F0A456680D3FB097C725D0AD66428347FBCF54B9ACCD9DFDEF545100EF788C01AEA8B955E66EB3E5E32469C044C7F578527F93DA0913DA7AF3A4FED96A0B206CA7C283D4805206677B9278C11FC3A29680B8622C17E885F3469D0C4C208117F52F642100AA63F010FFF7517BF9F3649C75D2A36D1AF5FE8B18D50C1A7851FFDB8CCA5F559A078F40259D61FDEF2C0FA16F25C3FA7EBED6673F83F101E74C2D273EE285FF00331300937F81A9E8F98B19D7FF9B349DF7A39A3D63011CD5B2F9D31FA62B581C9F174E7EBCA2E3F92C06FE07D4147657975EC1240000000049454E44AE426082")));
        dimension = toolkit.getScreenSize();
        setBounds((dimension.width - 290) / 2, (dimension.height - 130) / 2, 500, 180);

        setResizable(false);
        setLayout(null);
        //标题栏
        titlePanel.setBounds(0, 0, 500, 30);
        titlePanel.setBackground(new Color(110, 110, 110));
        titlePanel.setBorder(new MatteBorder(0, 0, 0, 0, new Color(110, 110, 110)));
        add(titlePanel);
        bodyPanel.setBounds(0, 0, 500, 180);
        bodyPanel.setBackground(new Color(255, 255, 255));
        bodyPanel.setBorder(new MatteBorder(0, 1, 1, 1, new Color(110, 110, 110)));
        add(bodyPanel);
        title_lable.setBounds(10, 0, 150, 30);
        title_lable.setFont(new Font("Microsoft Yahei", Font.BOLD, 13));
        title_lable.setForeground(new Color(255, 255, 255));
        add(title_lable, 0);
        //最小化以及关闭
        closeButton.setBounds(470, 0, 20, 30);
        closeButton.setFont(new Font("Microsoft Yahei",Font.BOLD,12));
        closeButton.setForeground(new Color(255,255,255));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setBorder(null);
        closeButton.setOpaque(false);
        closeButton.setIconTextGap(0);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusable(false);
        closeButton.addActionListener(this);
        add(closeButton,0);
        //拖拽功能
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                origin.x = e.getX();
                origin.y = e.getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - origin.x, p.y + e.getY()- origin.y);
            }
        });

        //主要面板

        // 來源目錄
        inputPathLable.setBounds(30, 40, 120, 25);
        inputPathLable.setFont(new Font("Microsoft Yahei",Font.BOLD,13));
        inputPathLable.setForeground(new Color(110,110,110));
        add(inputPathLable,0);
        inputPathField.setBounds(100, 40, 350, 25);
        inputPathField.setFont(new Font("Microsoft Yahei",Font.BOLD,13));
        inputPathField.setForeground(new Color(110,110,110));
        inputPathField.setEditable(false);
        add(inputPathField,0);
        inputPathButton.setBounds(450, 40, 10, 25);
        inputPathButton.setFont(new Font("Microsoft Yahei",Font.BOLD,13));
        inputPathButton.setForeground(new Color(110,110,110));
        inputPathButton.setMargin(new Insets(0, 0, 0, 0));
        inputPathButton.setOpaque(false);
        inputPathButton.setIconTextGap(0);
        inputPathButton.setContentAreaFilled(false);
        inputPathButton.setFocusable(false);
        inputPathButton.addActionListener(this);
        add(inputPathButton,0);

        //比較路徑
        comparePathLable.setBounds(30, 70, 100, 25);
        comparePathLable.setFont(new Font("Microsoft Yahei",Font.BOLD,13));
        comparePathLable.setForeground(new Color(110,110,110));
        add(comparePathLable,0);
        comparePathField.setBounds(100, 70, 350, 25);
        comparePathField.setFont(new Font("Microsoft Yahei",Font.BOLD,13));
        comparePathField.setForeground(new Color(110,110,110));
        comparePathField.setEditable(false);
        add(comparePathField,0);
        comparePathButton.setBounds(450, 70, 10, 25);
        comparePathButton.setFont(new Font("Microsoft Yahei",Font.BOLD,13));
        comparePathButton.setForeground(new Color(110,110,110));
        comparePathButton.setMargin(new Insets(0, 0, 0, 0));
        comparePathButton.setOpaque(false);
        comparePathButton.setIconTextGap(0);
        comparePathButton.setContentAreaFilled(false);
        comparePathButton.setFocusable(false);
        comparePathButton.addActionListener(this);
        add(comparePathButton,0);

        langLable.setBounds(30, 100, 100, 25);
        langLable.setFont(new Font("Microsoft Yahei",Font.BOLD,13));
        langLable.setForeground(new Color(110,110,110));
        add(langLable,0);
        langLableVal = new JComboBox<String>();
        langLableVal.addItem("JA");
        langLableVal.addItem("EN");
        langLableVal.addItem("DE");
        langLableVal.addItem("FR");
        langLableVal.addItem("CHS");
        langLableVal.setBounds(100, 100, 160, 23);
        langLableVal.setFont(new Font("Microsoft Yahei",Font.BOLD,13));
        langLableVal.setForeground(new Color(110,110,110));
        langLableVal.setOpaque(false);
        langLableVal.setFocusable(false);
        add(langLableVal,0);

        //拆包
        unpackButton.setBounds(30, 130, 80, 35);
        unpackButton.setFont(new Font("Microsoft Yahei",Font.PLAIN,25));
        unpackButton.setForeground(new Color(110,110,110));
        unpackButton.setMargin(new Insets(0, 0, 0, 0));
        unpackButton.setOpaque(false);
        unpackButton.setIconTextGap(0);
        unpackButton.setContentAreaFilled(false);
        unpackButton.setFocusable(false);
        unpackButton.addActionListener(this);
        unpackButton.setEnabled(true);
        add(unpackButton,0);

        //打包
        repackButton.setBounds(120, 130, 80, 35);
        repackButton.setFont(new Font("Microsoft Yahei",Font.PLAIN,25));
        repackButton.setForeground(new Color(110,110,110));
        repackButton.setMargin(new Insets(0, 0, 0, 0));
        repackButton.setOpaque(false);
        repackButton.setIconTextGap(0);
        repackButton.setContentAreaFilled(false);
        repackButton.setFocusable(false);
        repackButton.addActionListener(this);
        repackButton.setEnabled(true);
        add(repackButton,0);

        fontPatchButton.setBounds(280, 130, 160, 35);
        fontPatchButton.setFont(new Font("Microsoft Yahei",Font.PLAIN,25));
        fontPatchButton.setForeground(new Color(110,110,110));
        fontPatchButton.setMargin(new Insets(0, 0, 0, 0));
        fontPatchButton.setOpaque(false);
        fontPatchButton.setIconTextGap(0);
        fontPatchButton.setContentAreaFilled(false);
        fontPatchButton.setFocusable(false);
        fontPatchButton.addActionListener(this);
        fontPatchButton.setEnabled(true);
        add(fontPatchButton,0);

        setVisible(false);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == inputPathButton) {
            JFileChooser pathChooser = new JFileChooser();
            pathChooser.setDialogTitle("請選擇處理目錄...");
            pathChooser.setCurrentDirectory(new File(Config.getProperty("GamePath")));
            pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = pathChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filePath = pathChooser.getSelectedFile().getPath();
                inputPathField.setText(filePath);
                inputPathField.setCaretPosition(0);
            }
        }
        if(e.getSource() == comparePathButton) {
            JFileChooser pathChooser = new JFileChooser();
            pathChooser.setDialogTitle("請選擇處理目錄...");
            pathChooser.setCurrentDirectory(new File(Config.getProperty("GamePath")));
            pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = pathChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filePath = pathChooser.getSelectedFile().getPath();
                comparePathField.setText(filePath);
                comparePathField.setCaretPosition(0);
            }
        }
        if(e.getSource() == fontPatchButton) {
            String inputPath = inputPathField.getText();
            if (!hasFontSqpack(inputPath) ) {
                JOptionPane.showMessageDialog(null, "請選擇正確資源目錄,內含 000000.win32.dat0 ,000000.win32.index, 000000.win32.index2", "Error", JOptionPane.ERROR_MESSAGE);
            }else {
                Config.setProperty("GamePath", inputPath);
                Config.saveProperty();
                String[] resourceNames = {"000000.win32.dat0", "000000.win32.index", "000000.win32.index2"};
                for (String resourceName : resourceNames) {
                    File resourceFile = new File(inputPath + File.separator + resourceName);
                    if (resourceFile.exists() && resourceFile.isFile()) {
                        FileUtil.copyToFolder(resourceFile, "output" + File.separator + "output");
                    }
                }
                Config.setProperty("GamePath", inputPath);
                Config.saveProperty();
                PatchThread patchThread = new PatchThread(new File("output" + File.separator + "output").getAbsolutePath(), this);
                Thread patchFileThread = new Thread(patchThread);
                patchFileThread.start();
            }
        }
        if(e.getSource() == unpackButton) {
            String inputPath = inputPathField.getText();
            String comparePath = comparePathField.getText();
            if (!hasDataSqpack(inputPath) || !hasDataSqpack(comparePath)) {
                JOptionPane.showMessageDialog(null, "請選擇正確資源目錄,內含 0a0000.win32.dat0 ,0a0000.win32.index, 0a0000.win32.index2", "Error", JOptionPane.ERROR_MESSAGE);
            }else{
                String lang = Language.toLang((String) langLableVal.getSelectedItem());
                Config.setProperty("GamePath", inputPath);
                Config.setProperty("GameComparePath", comparePath);
                Config.setProperty("Language", Language.toLang(lang));
                Config.saveProperty();
                UnpackThread unpackThread = new UnpackThread(inputPath, comparePath, this);
                Thread unpackFileThread = new Thread(unpackThread);
                unpackFileThread.start();
            }
        }

        if(e.getSource() == repackButton) {
            String inputPath = inputPathField.getText();
            if (!hasDataSqpack(inputPath)) {
                JOptionPane.showMessageDialog(null, "請選擇正確資源目錄,內含 0a0000.win32.dat0 ,0a0000.win32.index, 0a0000.win32.index2", "Error", JOptionPane.ERROR_MESSAGE);
            }else {
                String[] resourceNames = {"0a0000.win32.dat0", "0a0000.win32.index", "0a0000.win32.index2"};
                for (String resourceName : resourceNames) {
                    File resourceFile = new File(inputPath + File.separator + resourceName);
                    if (resourceFile.exists() && resourceFile.isFile()) {
                        FileUtil.copyToFolder(resourceFile, "output" + File.separator + "output");
                    }
                }
                String lang = Language.toLang((String) langLableVal.getSelectedItem());
                Config.setProperty("GamePath", inputPath);
                Config.setProperty("Language", Language.toLang(lang));
                Config.saveProperty();
                RepackThread repackThread = new RepackThread(new File("output" + File.separator + "output").getAbsolutePath(), this);
                Thread repackFileThread = new Thread(repackThread);
                repackFileThread.start();
            }
        }

        if (e.getSource() == closeButton) {
            System.exit(0);
        }
    }

    private boolean hasFontSqpack(String path){
        if(path == null)
            return false;
        String[] resourceNames = {"000000.win32.dat0", "000000.win32.index", "000000.win32.index2"};
        for(String resourceName :resourceNames){
            File resourceFile = new File(path + File.separator + resourceName);
            if(!resourceFile.exists() || !resourceFile.isFile()){
                return false;
            }
        }
        return true;
    }
    private boolean hasDataSqpack(String path){
        if(path == null)
            return false;
        String[] resourceNames = {"0a0000.win32.dat0", "0a0000.win32.index", "0a0000.win32.index2"};
        for(String resourceName :resourceNames){
            File resourceFile = new File(path + File.separator + resourceName);
            if(!resourceFile.exists() || !resourceFile.isFile()){
                return false;
            }
        }
        return true;
    }
}
