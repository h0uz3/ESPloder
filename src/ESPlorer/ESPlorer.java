package ESPlorer;

import jssc.*;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class ESPlorer extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(ESPlorer.class.getName());
    public static final FileNameExtensionFilter FILTER_LUA = new FileNameExtensionFilter("LUA files (*.lua, *.lc)", Constants.EXTENSION_LUA);
    private static final FileNameExtensionFilter FILTER_PYTHON = new FileNameExtensionFilter("Python files (*.py)", Constants.EXTENSION_PY);
    /* Snippets */
    private static final String[] Snippets = new String[16];
    private static boolean pOpen = false;
    private static boolean portJustOpen = false;
    private static ArrayList<String> lookAndFeel;
    private static ArrayList<String> LAFclass;
    private static Preferences prefs;
    private static int j = 0;
    private static String[] s;
    private static String rcvBuf = "";
    private static String rx_data = "";
    private static byte[] rx_byte;
    private static byte[] tx_byte;
    private static String DownloadCommand;
    // downloader end
    private static boolean busyIcon = false;
    private static SerialPort serialPort;
    private static int LogMax = 10 * 1024;
    private static int TerminalMax = 100 * 1024;
    private static int iSnippets = 0;
    private static ImageIcon LED_GREY;
    private static ImageIcon LED_GREEN;
    private static ImageIcon LED_RED;
    private static ImageIcon LED_BLUE;
    private static boolean LocalEcho = true;
    private final int SendPacketSize = 250;
    private final String NewFile = "New";
    /* Files tab end */
    private int nSpeed = 9600;
    private ActionListener taskPerformer;
    private Timer timer;
    private Timer timeout;
    private Timer openTimeout;
    private Color themeTextBackground;
    private ArrayList<String> sendBuf;
    // downloader
    private int packets = 0;
    private String rcvFile = "";
    private ArrayList<String> rcvPackets;
    private ArrayList<byte[]> sendPackets;
    private ArrayList<Boolean> sendPacketsCRC;
    private ArrayList<String> PacketsData;
    private ArrayList<Integer> PacketsSize;
    private ArrayList<Integer> PacketsCRC;
    private ArrayList<Integer> PacketsNum;
    private byte[] PacketsByte;
    private URI donate_uri;
    private URI homepage_uri;
    private URI api_cn_uri;
    private URI api_en_uri;
    private URI api_ru_uri;
    private URI changelog_uri;
    private URI nodemcu_download_latest_uri;
    private URI nodemcu_download_dev_uri;
    private URI flasher_uri;
    private URI buy_nodeMCU;
    private URI buy_esp8266;
    private URI buy_esd12;
    private URI buy_other;
    private URI esp8266com_uri;
    private URI esp8266ru_uri;
    private URI esplorer_latest;
    private URI esplorer_source;
    //  String s = new String();
    private String FileName = "script"; // without ext
    private String DownloadedFileName = "";
    private int FileCount = 0;
    private String workDir = "";
    private JFileChooser chooser;
    private FileInputStream fis = null;
    private FileOutputStream fos = null;
    private OutputStreamWriter osw = null;
    private BufferedWriter bw = null;

    private JDialog aboutDialog;
    private JCheckBoxMenuItem AlwaysOnTop;
    private JSlider AnswerDelay;
    private JLabel AnswerDelayLabel;
    private JCheckBox AutoScroll;
    private JCheckBox AutodetectFirmware;
    private JLabel Busy;
    private JButton copyButton;
    private JButton ButtonCut;
    private JButton ButtonFileClose;
    private JButton ButtonFileNew;
    private JButton ButtonFileReload;
    private JButton ButtonFileSave;
    private JButton ButtonPaste;
    private JButton ButtonRedo;
    private JButton ButtonSendLine;
    private JButton ButtonSendSelected;
    private JButton ButtonSnippet0;
    private JButton ButtonSnippet1;
    private JButton ButtonSnippet10;
    private JButton ButtonSnippet11;
    private JButton ButtonSnippet12;
    private JButton ButtonSnippet13;
    private JButton ButtonSnippet14;
    private JButton ButtonSnippet15;
    private JButton ButtonSnippet2;
    private JButton ButtonSnippet3;
    private JButton ButtonSnippet4;
    private JButton ButtonSnippet5;
    private JButton ButtonSnippet6;
    private JButton ButtonSnippet7;
    private JButton ButtonSnippet8;
    private JButton ButtonSnippet9;
    private JButton ButtonUndo;
    private JCheckBox CR;
    private JComboBox<String> Command;
    private JCheckBox Condensed;
    private JPopupMenu ContextMenuEditor;
    private JTextField CustomPortName;
    private JSlider Delay;
    private JLabel DelayLabel;
    private JButton DonateSmall;
    private JCheckBox DumbMode;
    private JCheckBox EOL;
    private JComboBox EditorTheme;
    private JButton FileAsButton1;
    private JCheckBox FileAutoRun;
    private JCheckBox FileAutoSaveDisk;
    private JCheckBox FileAutoSaveESP;
    private JButton FileDo;
    private JButton FileFormat;
    private JButton FileListReload;
    JLayeredPane FileManagerPane;
    private JScrollPane FileManagerScrollPane;
    private JLabel FilePathLabel;
    private JTextField FileRename;
    private JLabel FileRenameLabel;
    private JLayeredPane FileRenamePanel;
    private JToggleButton FileSaveESP;
    private JToggleButton FileSendESP;
    private JButton FileSystemInfo;
    private JTabbedPane FilesTabbedPane;
    private JToolBar FilesToolBar;
    private JCheckBox LF;
    private JLayeredPane LeftBasePane;
    private JLayeredPane LeftExtraButtons;
    private JTabbedPane LeftTab;
    private JSlider LineDelay;
    private JLabel LineDelayLabel;
    private JTextArea Log;
    private JTextField LogMaxSize;
    private JMenuItem MenuItemESPFormat;
    private JMenuItem MenuItemESPReset;
    private JMenuItem MenuItemEditCopy;
    private JMenuItem MenuItemEditCut;
    private JMenuItem MenuItemEditPaste;
    private JMenuItem MenuItemEditRedo;
    private JMenuItem MenuItemEditSendLine;
    private JMenuItem MenuItemEditSendSelected;
    private JMenuItem MenuItemEditUndo;
    private JMenuItem MenuItemEditorCopy;
    private JMenuItem MenuItemEditorCut;
    private JMenuItem MenuItemEditorPaste;
    private JMenuItem MenuItemEditorRedo;
    private JMenuItem MenuItemEditorSendLine;
    private JMenuItem MenuItemEditorSendSelected;
    private JMenuItem MenuItemEditorUndo;
    private JMenuItem MenuItemFileClose;
    private JMenuItem MenuItemFileDo;
    private JMenuItem MenuItemFileNew;
    private JMenuItem MenuItemFileOpen;
    private JMenuItem MenuItemFileReload;
    private JMenuItem MenuItemFileRemoveESP;
    private JMenuItem MenuItemFileSave;
    private JMenuItem MenuItemFileSaveAll;
    private JMenuItem MenuItemFileSaveAs;
    private JMenuItem MenuItemFileSaveESP;
    private JMenuItem MenuItemFileSendESP;
    private JMenuItem MenuItemHelpAbout;
    private JMenuItem MenuItemLogClear;
    private JMenuItem MenuItemTerminalClear;
    private JMenuItem MenuItemTerminalCopy;
    private JMenuItem MenuItemTerminalFontDec;
    private JMenuItem MenuItemTerminalFontInc;
    private JMenuItem MenuItemTerminalFormat;
    private JMenuItem MenuItemTerminalReset;
    private JCheckBoxMenuItem MenuItemViewDonate;
    private JMenuItem MenuItemViewEditorFontDec;
    private JMenuItem MenuItemViewEditorFontInc;
    private JCheckBoxMenuItem MenuItemViewFileManager;
    private JRadioButtonMenuItem MenuItemViewLF1;
    private JCheckBoxMenuItem MenuItemViewLeftExtra;
    private JCheckBoxMenuItem MenuItemViewLog;
    private JMenuItem MenuItemViewLogFontDec;
    private JMenuItem MenuItemViewLogFontInc;
    private JCheckBoxMenuItem MenuItemViewRightExtra;
    private JCheckBoxMenuItem MenuItemViewSnippets;
    private JMenuItem MenuItemViewTermFontDec;
    private JMenuItem MenuItemViewTermFontInc;
    private JCheckBoxMenuItem MenuItemViewToolbar;
    private JMenu MenuView;
    private JButton NodeReset;
    private JToggleButton Open;
    private JRadioButton OptionMicroPython;
    private JRadioButton OptionNodeMCU;
    private JTextField PASS;
    private JComboBox<String> Port;
    private JLabel PortCTS;
    private JToggleButton PortDTR;
    private JLabel PortOpenLabel;
    private JToggleButton PortRTS;
    private JProgressBar ProgressBar;
    private JButton ReScan;
    private JLayeredPane RightExtraButtons;
    private JSplitPane RightFilesSplitPane;
    private JLayeredPane RightSnippetsPane;
    private JSplitPane RightSplitPane;
    private JTextField SSID;
    private JScrollPane ScrollLog;
    private JButton SendCommand;
    private JComboBox ServerMode;
    private JTextField ServerPort;
    private JButton SnippetCancelEdit;
    private JButton SnippetEdit0;
    private JButton SnippetEdit1;
    private JButton SnippetEdit10;
    private JButton SnippetEdit11;
    private JButton SnippetEdit13;
    private JButton SnippetEdit14;
    private JButton SnippetEdit15;
    private JButton SnippetEdit2;
    private JButton SnippetEdit3;
    private JButton SnippetEdit4;
    private JButton SnippetEdit5;
    private JButton SnippetEdit6;
    private JButton SnippetEdit7;
    private JButton SnippetEdit8;
    private JButton SnippetEdit9;
    private JTextField SnippetName;
    private JButton SnippetRun;
    private JButton SnippetSave;
    private org.fife.ui.rtextarea.RTextScrollPane SnippetScrollPane;
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea SnippetText;
    private JLayeredPane SnippetTopPane;
    private JLabel SnippetsBusy;
    private JComboBox Speed;
    private JLayeredPane TCP_common;
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea Terminal;
    private JTextField TerminalMaxSize;
    private JTabbedPane TextTab;
    private JComboBox TimerNumber;
    private JCheckBox TurboMode;
    private JCheckBox UseCustomPortName;
    private JCheckBox UseExternalEditor;
    private JLayeredPane WiFi_common;
    private ButtonGroup buttonGroupLF;
    private JButton cmdNodeRestart;
    private JComboBox conn_id;
    private JTextArea data;
    private JRadioButton multi;
    private JComboBox protocol;
    private JTextField remote_address;
    private JTextField remote_port;
    private JTextField udp_local_port;
    private JTextField udp_mode;
    /* Files tab start */
    private ArrayList<JLayeredPane> FileLayeredPane1;
    private ArrayList<org.fife.ui.rsyntaxtextarea.RSyntaxTextArea> TextEditor1;
    private ArrayList<org.fife.ui.rtextarea.RTextScrollPane> TextScroll1;
    private ArrayList<GroupLayout> FileLayeredPaneLayout1;
    private ArrayList<org.fife.ui.autocomplete.CompletionProvider> provider;
    private ArrayList<org.fife.ui.autocomplete.AutoCompletion> ac;
    private ArrayList<File> iFile; // for files in tab
    private ArrayList<File> mFile; // for multifile op
    private ArrayList<Boolean> FileChanged;
    private ArrayList<JButton> FileAsButton;
    private ArrayList<JPopupMenu> FilePopupMenu;
    private ArrayList<JMenuItem> FilePopupMenuItem;
    private int iTab = 0; // tab index
    private int mFileIndex = -1; // multifile index
    private long startTime = System.currentTimeMillis();

    public ESPlorer() {
        try {
            FileHandler fh = new FileHandler("ESPlorer.Log");
            LOGGER.addHandler(fh);
        } catch (SecurityException e) {
            LOGGER.log(Level.SEVERE, "Internal error 105: Can't create log file. Permission denied.", e);
//            LOGGER.log(Level.SEVERE,e.getStackTrace().toString(), e);
//            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Internal error 106: Can't create log file. I/O error.", e);
//            LOGGER.log(Level.SEVERE,e.getStackTrace().toString(), e);
//            e.printStackTrace();
        }
        initComponents();
        FinalInit();

    }

    public static void main(String args[]) {
        lookAndFeel = new ArrayList<String>();
        LAFclass = new ArrayList<String>();
        String laf;
        prefs = Preferences.userRoot().node(Constants.nodeRoot);
        laf = prefs.get("LAF", "plaf.nimbus.NimbusLookAndFeel");
        lookAndFeel.add("Nimbus");
        LAFclass.add("plaf.nimbus.NimbusLookAndFeel");
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (laf.equals(info.getClassName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    Logger.getLogger(ESPlorer.class.getName()).log(Level.INFO, "DEFAULT: L&F " + info.getName() + " class:" + info.getClassName());
                } else {
                    Logger.getLogger(ESPlorer.class.getName()).log(Level.INFO, "Installed: L&F " + info.getName() + " class:" + info.getClassName());
                }
                if (!"Nimbus".equals(info.getName())) {
                    lookAndFeel.add(info.getName());
                    LAFclass.add(info.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ESPlorer.class.getName()).log(Level.INFO, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ESPlorer.class.getName()).log(Level.INFO, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ESPlorer.class.getName()).log(Level.INFO, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ESPlorer.class.getName()).log(Level.INFO, null, ex);
        }
        //</editor-fold>
        try {
        /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ESPlorer().setVisible(true);
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(ESPlorer.class.getName()).log(Level.INFO, null, ex);
        }
    }

    private void initComponents() {
        org.jdesktop.beansbinding.BindingGroup bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        JPopupMenu contextMenuTerminal = new JPopupMenu();
        MenuItemTerminalClear = new JMenuItem();
        MenuItemTerminalCopy = new JMenuItem();
        JPopupMenu.Separator terminalSeparator1 = new JPopupMenu.Separator();
        MenuItemTerminalFontInc = new JMenuItem();
        MenuItemTerminalFontDec = new JMenuItem();
        JPopupMenu.Separator terminalSeparator2 = new JPopupMenu.Separator();
        MenuItemTerminalReset = new JMenuItem();
        MenuItemTerminalFormat = new JMenuItem();
        ContextMenuEditor = new JPopupMenu();
        MenuItemEditorUndo = new JMenuItem();
        MenuItemEditorRedo = new JMenuItem();
        JPopupMenu.Separator editorSeparator1 = new JPopupMenu.Separator();
        MenuItemEditorSendSelected = new JMenuItem();
        MenuItemEditorSendLine = new JMenuItem();
        JPopupMenu.Separator editorSeparator = new JPopupMenu.Separator();
        MenuItemEditorCut = new JMenuItem();
        MenuItemEditorCopy = new JMenuItem();
        MenuItemEditorPaste = new JMenuItem();
        JPopupMenu.Separator editorSeparator2 = new JPopupMenu.Separator();
        JMenuItem menuItemEditorFontInc = new JMenuItem();
        JMenuItem menuItemEditorFontDec = new JMenuItem();
        JPopupMenu contextMenuLog = new JPopupMenu();
        MenuItemLogClear = new JMenuItem();
        JMenuItem menuItemLogClose = new JMenuItem();
        JPopupMenu.Separator logSeparator = new JPopupMenu.Separator();
        JMenuItem menuItemLogFontInc = new JMenuItem();
        JMenuItem menuItemLogFontDec = new JMenuItem();
        ButtonGroup MUXGroup = new ButtonGroup();
        ButtonGroup firmware = new ButtonGroup();
        aboutDialog = new JDialog();
        JLabel appName = new JLabel();
        JLabel version1 = new JLabel();
        JButton donate = new JButton();
        JLabel author = new JLabel();
        JButton homePage = new JButton();
        JPopupMenu contextMenuESPFileLUA = new JPopupMenu();
        JMenuItem menuItemESPFileDo = new JMenuItem();
        JPopupMenu.Separator terminalSeparator3 = new JPopupMenu.Separator();
        JMenuItem menuItemESPFileDelete = new JMenuItem();
        buttonGroupLF = new ButtonGroup();
        JSplitPane horizontSplit = new JSplitPane();
        LeftBasePane = new JLayeredPane();
        LeftTab = new JTabbedPane();
        JPanel nodeMCU = new JPanel();
        TextTab = new JTabbedPane();
        JLayeredPane sriptsTab = new JLayeredPane();
        FilesToolBar = new JToolBar();
        ButtonFileNew = new JButton();
        JButton buttonFileOpen = new JButton();
        ButtonFileReload = new JButton();
        ButtonFileSave = new JButton();
        JButton buttonFileSaveAll = new JButton();
        ButtonFileClose = new JButton();
        JToolBar.Separator jSeparator1 = new JToolBar.Separator();
        ButtonUndo = new JButton();
        ButtonRedo = new JButton();
        JToolBar.Separator jSeparator8 = new JToolBar.Separator();
        ButtonCut = new JButton();
        copyButton = new JButton();
        ButtonPaste = new JButton();
        JToolBar.Separator jSeparator6 = new JToolBar.Separator();
        ButtonSendSelected = new JButton();
        ButtonSendLine = new JButton();
        FilesTabbedPane = new JTabbedPane();
        JLayeredPane fileLayeredPane = new JLayeredPane();
        org.fife.ui.rtextarea.RTextScrollPane textScroll = new org.fife.ui.rtextarea.RTextScrollPane();
        org.fife.ui.rsyntaxtextarea.RSyntaxTextArea textEditor = new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea();
        LeftExtraButtons = new JLayeredPane();
        JButton fileDo1 = new JButton();
        JButton fileCompile = new JButton();
        JButton fileSaveCompileDoLC = new JButton();
        JButton fileCompileDoLC = new JButton();
        JButton fileCompile1 = new JButton();
        Busy = new JLabel();
        FilePathLabel = new JLabel();
        ProgressBar = new JProgressBar();
        JLayeredPane leftMainButtons = new JLayeredPane();
        FileSaveESP = new JToggleButton();
        FileSendESP = new JToggleButton();
        FileDo = new JButton();
        JButton filesUpload = new JButton();
        JLayeredPane nodeMCUCommands = new JLayeredPane();
        cmdNodeRestart = new JButton();
        JButton cmdNodeChipID = new JButton();
        JButton cmdNodeHeap = new JButton();
        JButton cmdNodeSleep = new JButton();
        JButton cmdListFiles = new JButton();
        JButton cmdTimerStop = new JButton();
        TimerNumber = new JComboBox();
        JLayeredPane jLayeredPane1 = new JLayeredPane();
        JLayeredPane nodeMCUSnippets = new JLayeredPane();
        JLayeredPane leftSnippetsPane = new JLayeredPane();
        SnippetEdit0 = new JButton();
        SnippetEdit1 = new JButton();
        SnippetEdit2 = new JButton();
        SnippetEdit3 = new JButton();
        SnippetEdit4 = new JButton();
        SnippetEdit5 = new JButton();
        SnippetEdit6 = new JButton();
        SnippetEdit7 = new JButton();
        SnippetEdit8 = new JButton();
        SnippetEdit9 = new JButton();
        SnippetEdit10 = new JButton();
        SnippetEdit11 = new JButton();
        JButton snippetEdit12 = new JButton();
        SnippetEdit13 = new JButton();
        SnippetEdit14 = new JButton();
        SnippetEdit15 = new JButton();
        SnippetTopPane = new JLayeredPane();
        SnippetName = new JTextField();
        SnippetSave = new JButton();
        SnippetRun = new JButton();
        SnippetsBusy = new JLabel();
        SnippetCancelEdit = new JButton();
        Condensed = new JCheckBox();
        SnippetScrollPane = new org.fife.ui.rtextarea.RTextScrollPane();
        SnippetText = new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea();
        JLayeredPane nodeMCUSettings = new JLayeredPane();
        JLayeredPane optionsFirmware = new JLayeredPane();
        OptionNodeMCU = new JRadioButton();
        OptionMicroPython = new JRadioButton();
        JLayeredPane optionsOther = new JLayeredPane();
        FileAutoSaveDisk = new JCheckBox();
        FileAutoSaveESP = new JCheckBox();
        FileAutoRun = new JCheckBox();
        JLabel editorThemeLabel = new JLabel();
        EditorTheme = new JComboBox();
        UseExternalEditor = new JCheckBox();
        JLayeredPane optionsFileSendMode = new JLayeredPane();
        DelayLabel = new JLabel();
        Delay = new JSlider();
        AnswerDelayLabel = new JLabel();
        AnswerDelay = new JSlider();
        DumbMode = new JCheckBox();
        LineDelayLabel = new JLabel();
        LineDelay = new JSlider();
        TurboMode = new JCheckBox();
        JLayeredPane jLayeredPane2 = new JLayeredPane();
        JLabel jLabel1 = new JLabel();
        JLabel jLabel7 = new JLabel();
        TerminalMaxSize = new JTextField();
        LogMaxSize = new JTextField();
        JLabel jLabel8 = new JLabel();
        JLabel jLabel9 = new JLabel();
        JLayeredPane jLayeredPane3 = new JLayeredPane();
        CustomPortName = new JTextField();
        UseCustomPortName = new JCheckBox();
        JLabel jLabel10 = new JLabel();
        AutodetectFirmware = new JCheckBox();
        Box.Filler topWiFiStaFiller = new Box.Filler(new Dimension(457, 150), new Dimension(457, 150), new Dimension(457, 150));
        JButton cmdGetCWJAP = new JButton();
        JButton cmdSetCWJAP = new JButton();
        SSID = new JTextField();
        PASS = new JTextField();
        JButton cmdSetCWQAP = new JButton();
        JButton cmdGetCIPSTAMAC = new JButton();
        JButton cmdSetCIPSTAMAC = new JButton();
        JFormattedTextField MAC = new JFormattedTextField();
        JButton cmdGetCIPSTA = new JButton();
        JButton cmdSetCIPSTA = new JButton();
        JTextField stationIP = new JTextField();
        JButton cmdSetCWSAP = new JButton();
        JButton cmdGetCWSAP = new JButton();
        JButton cmdGetCIPAPMAC = new JButton();
        JButton cmdGetCWLIF = new JButton();
        JLayeredPane TCPclientBottomPane = new JLayeredPane();
        JPanel common = new JPanel();
        conn_id = new JComboBox();
        JRadioButton single = new JRadioButton();
        multi = new JRadioButton();
        JLabel jLabel4 = new JLabel();
        JButton cmdGetCIPSTART = new JButton();
        JLayeredPane UDP = new JLayeredPane();
        udp_local_port = new JTextField();
        JLabel jLabel2 = new JLabel();
        udp_mode = new JTextField();
        JLabel jLabel3 = new JLabel();
        remote_address = new JTextField();
        remote_port = new JTextField();
        protocol = new JComboBox();
        JButton cmdSetCIPSTART = new JButton();
        JScrollPane jScrollData = new JScrollPane();
        data = new JTextArea();
        JButton cmdCIPSEND = new JButton();
        JButton cmdCIPSENDinteractive = new JButton();
        JButton cmdSetCIPCLOSE = new JButton();
        JPanel AT_Server = new JPanel();
        Box.Filler TCPServerTopFiller = new Box.Filler(new Dimension(457, 150), new Dimension(457, 150), new Dimension(457, 150));
        JLayeredPane TCPServerBottomPane = new JLayeredPane();
        JButton cmdGetCIPMODE = new JButton();
        JButton cmdSetCIPMODE0 = new JButton();
        JButton cmdSetCIPMODE1 = new JButton();
        JButton cmdSetCIPSERVER = new JButton();
        ServerMode = new JComboBox();
        JLabel jLabel5 = new JLabel();
        ServerPort = new JTextField();
        JButton cmdGetCIPSTO = new JButton();
        JButton cmdSetCIPSTO = new JButton();
        JTextField serverTimeout = new JTextField();
        JLabel jLabel6 = new JLabel();
        TCP_common = new JLayeredPane();
        WiFi_common = new JLayeredPane();
        JButton cmdGetHelpCWMODE = new JButton();
        JButton cmdSetCWMODE1 = new JButton();
        JButton cmdSetCWMODE2 = new JButton();
        JButton cmdSetCWLAP = new JButton();
        JButton cmdSetCWMODE3 = new JButton();
        JComboBox DHCP = new JComboBox();
        JComboBox DHCPmode = new JComboBox();
        JLabel comingSoon1 = new JLabel();
        JLayeredPane rightBasePane = new JLayeredPane();
        JLayeredPane LEDPanel = new JLayeredPane();
        PortOpenLabel = new JLabel();
        PortCTS = new JLabel();
        PortDTR = new JToggleButton();
        PortRTS = new JToggleButton();
        Open = new JToggleButton();
        Speed = new JComboBox();
        ReScan = new JButton();
        AutoScroll = new JCheckBox();
        Port = new JComboBox();
        EOL = new JCheckBox();
        JLayeredPane rightBottomPane = new JLayeredPane();
        LF = new JCheckBox();
        SendCommand = new JButton();
        CR = new JCheckBox();
        Command = new JComboBox();
        JLayeredPane rightBigPane = new JLayeredPane();
        RightFilesSplitPane = new JSplitPane();
        JLayeredPane terminalLogPane = new JLayeredPane();
        RightSplitPane = new JSplitPane();
        org.fife.ui.rtextarea.RTextScrollPane terminalPane = new org.fife.ui.rtextarea.RTextScrollPane();
        Terminal = new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea();
        ScrollLog = new JScrollPane();
        Log = new JTextArea();
        FileManagerScrollPane = new JScrollPane();
        FileManagerPane = new JLayeredPane();
        FileFormat = new JButton();
        FileSystemInfo = new JButton();
        FileListReload = new JButton();
        FileAsButton1 = new JButton();
        FileRenamePanel = new JLayeredPane();
        FileRenameLabel = new JLabel();
        FileRename = new JTextField();
        RightSnippetsPane = new JLayeredPane();
        ButtonSnippet0 = new JButton();
        ButtonSnippet1 = new JButton();
        ButtonSnippet2 = new JButton();
        ButtonSnippet3 = new JButton();
        ButtonSnippet4 = new JButton();
        ButtonSnippet5 = new JButton();
        ButtonSnippet6 = new JButton();
        ButtonSnippet7 = new JButton();
        ButtonSnippet8 = new JButton();
        ButtonSnippet9 = new JButton();
        ButtonSnippet10 = new JButton();
        ButtonSnippet11 = new JButton();
        ButtonSnippet12 = new JButton();
        ButtonSnippet13 = new JButton();
        ButtonSnippet14 = new JButton();
        ButtonSnippet15 = new JButton();
        JLabel logo = new JLabel();
        RightExtraButtons = new JLayeredPane();
        JButton nodeHeap = new JButton();
        JButton nodeInfo = new JButton();
        JButton nodeChipID = new JButton();
        JButton nodeFlashID = new JButton();
        NodeReset = new JButton();
        DonateSmall = new JButton();
        JMenuBar mainMenuBar = new JMenuBar();
        JMenu menuFile = new JMenu();
        MenuItemFileNew = new JMenuItem();
        MenuItemFileOpen = new JMenuItem();
        MenuItemFileReload = new JMenuItem();
        MenuItemFileSave = new JMenuItem();
        MenuItemFileSaveAs = new JMenuItem();
        MenuItemFileSaveAll = new JMenuItem();
        MenuItemFileClose = new JMenuItem();
        JPopupMenu.Separator jSeparatorFileMenu = new JPopupMenu.Separator();
        MenuItemFileSaveESP = new JMenuItem();
        JMenuItem jMenuItem4 = new JMenuItem();
        MenuItemFileSendESP = new JMenuItem();
        JMenuItem jMenuItem7 = new JMenuItem();
        JPopupMenu.Separator jSeparator4 = new JPopupMenu.Separator();
        MenuItemFileDo = new JMenuItem();
        JPopupMenu.Separator jSeparator3 = new JPopupMenu.Separator();
        MenuItemFileRemoveESP = new JMenuItem();
        JPopupMenu.Separator jSeparator2 = new JPopupMenu.Separator();
        JMenuItem menuItemFileExit = new JMenuItem();
        JMenu menuEdit = new JMenu();
        MenuItemEditUndo = new JMenuItem();
        MenuItemEditRedo = new JMenuItem();
        JPopupMenu.Separator jSeparator7 = new JPopupMenu.Separator();
        MenuItemEditCut = new JMenuItem();
        MenuItemEditCopy = new JMenuItem();
        MenuItemEditPaste = new JMenuItem();
        JPopupMenu.Separator jSeparator5 = new JPopupMenu.Separator();
        MenuItemEditSendSelected = new JMenuItem();
        MenuItemEditSendLine = new JMenuItem();
        JMenu menuESP = new JMenu();
        MenuItemESPReset = new JMenuItem();
        MenuItemESPFormat = new JMenuItem();
        MenuView = new JMenu();
        AlwaysOnTop = new JCheckBoxMenuItem();
        MenuItemViewLog = new JCheckBoxMenuItem();
        JMenuItem menuItemViewClearLog = new JMenuItem();
        JMenuItem menuItemViewClearTerminal = new JMenuItem();
        JPopupMenu.Separator jSeparator9 = new JPopupMenu.Separator();
        MenuItemViewToolbar = new JCheckBoxMenuItem();
        MenuItemViewLeftExtra = new JCheckBoxMenuItem();
        MenuItemViewSnippets = new JCheckBoxMenuItem();
        MenuItemViewFileManager = new JCheckBoxMenuItem();
        MenuItemViewRightExtra = new JCheckBoxMenuItem();
        MenuItemViewDonate = new JCheckBoxMenuItem();
        JPopupMenu.Separator jSeparator13 = new JPopupMenu.Separator();
        MenuItemViewTermFontInc = new JMenuItem();
        MenuItemViewTermFontDec = new JMenuItem();
        JPopupMenu.Separator jSeparator10 = new JPopupMenu.Separator();
        MenuItemViewEditorFontInc = new JMenuItem();
        MenuItemViewEditorFontDec = new JMenuItem();
        JPopupMenu.Separator jSeparator11 = new JPopupMenu.Separator();
        MenuItemViewLogFontInc = new JMenuItem();
        MenuItemViewLogFontDec = new JMenuItem();
        JPopupMenu.Separator jSeparator12 = new JPopupMenu.Separator();
        JMenuItem menuItemViewFontDefault = new JMenuItem();
        JPopupMenu.Separator jSeparator17 = new JPopupMenu.Separator();
        MenuItemViewLF1 = new JRadioButtonMenuItem();
        JMenu menuLinks = new JMenu();
        JMenuItem menuItemLinksAPIcn = new JMenuItem();
        JMenuItem menuItemLinksAPIen = new JMenuItem();
        JMenuItem menuItemLinksAPIru = new JMenuItem();
        JMenuItem menuItemLinksChangelog = new JMenuItem();
        JPopupMenu.Separator jSeparator14 = new JPopupMenu.Separator();
        JMenuItem menuItemLinksDownloadLatestFirmware = new JMenuItem();
        JMenuItem menuItemLinksDownloadLatestDev = new JMenuItem();
        JMenuItem menuItemLinksDownloadLatestFlasher = new JMenuItem();
        JPopupMenu.Separator jSeparator15 = new JPopupMenu.Separator();
        JMenuItem menuItemLinksBuyDevBoard = new JMenuItem();
        JMenuItem menuItemLinksBuyESP8266 = new JMenuItem();
        JMenuItem menuItemLinksBuyESD12 = new JMenuItem();
        JMenuItem menuItemLinksBuyOther = new JMenuItem();
        JPopupMenu.Separator jSeparator16 = new JPopupMenu.Separator();
        JMenuItem menuItemLinksESPlorerForumEn = new JMenuItem();
        JMenuItem menuItemLinksESPlorerForumRu = new JMenuItem();
        JMenuItem menuItemLinksESPlorerLatest = new JMenuItem();
        JMenuItem menuItemLinksESPlorerSource = new JMenuItem();
        JMenuItem menuItemLinksESPlorerHome = new JMenuItem();
        JMenuItem menuItemLinksDonate = new JMenuItem();
        JMenu menuHelp = new JMenu();
        MenuItemHelpAbout = new JMenuItem();

        contextMenuTerminal.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                ContextMenuTerminalPopupMenuWillBecomeVisible();
            }
        });

        MenuItemTerminalClear.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemTerminalClear.setIcon(new ImageIcon(getClass().getResource("/resources/terminal_clear.png"))); // NOI18N
        MenuItemTerminalClear.setText("Clear");
        MenuItemTerminalClear.setToolTipText("");
        MenuItemTerminalClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        contextMenuTerminal.add(MenuItemTerminalClear);

        MenuItemTerminalCopy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemTerminalCopy.setIcon(new ImageIcon(getClass().getResource("/resources/copy.png"))); // NOI18N
        MenuItemTerminalCopy.setText("Copy");
        MenuItemTerminalCopy.setToolTipText("Copy selected text to system clipboard");
        MenuItemTerminalCopy.setEnabled(false);
        MenuItemTerminalCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        contextMenuTerminal.add(MenuItemTerminalCopy);
        contextMenuTerminal.add(terminalSeparator1);

        MenuItemTerminalFontInc.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemTerminalFontInc.setIcon(new ImageIcon(getClass().getResource("/resources/zoom in.png"))); // NOI18N
        MenuItemTerminalFontInc.setText("Inc font size");
        MenuItemTerminalFontInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemTerminalFontIncActionPerformed();
            }
        });
        contextMenuTerminal.add(MenuItemTerminalFontInc);

        MenuItemTerminalFontDec.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemTerminalFontDec.setIcon(new ImageIcon(getClass().getResource("/resources/zoom out.png"))); // NOI18N
        MenuItemTerminalFontDec.setText("Dec font size");
        MenuItemTerminalFontDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemTerminalFontDecActionPerformed();
            }
        });
        contextMenuTerminal.add(MenuItemTerminalFontDec);
        contextMenuTerminal.add(terminalSeparator2);

        MenuItemTerminalReset.setIcon(new ImageIcon(getClass().getResource("/resources/reset.png"))); // NOI18N
        MenuItemTerminalReset.setText("Restart ESP module");
        MenuItemTerminalReset.setToolTipText("Send RESET command (firmware depended)");
        MenuItemTerminalReset.setEnabled(false);
        MenuItemTerminalReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemTerminalResetActionPerformed();
            }
        });
        contextMenuTerminal.add(MenuItemTerminalReset);

        MenuItemTerminalFormat.setIcon(new ImageIcon(getClass().getResource("/resources/file manager (delete).png"))); // NOI18N
        MenuItemTerminalFormat.setText("Format ESP");
        MenuItemTerminalFormat.setToolTipText("Remove All files from ESP flash memory");
        MenuItemTerminalFormat.setEnabled(false);
        MenuItemTerminalFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemTerminalFormatActionPerformed();
            }
        });
        contextMenuTerminal.add(MenuItemTerminalFormat);

        MenuItemEditorUndo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditorUndo.setIcon(new ImageIcon(getClass().getResource("/resources/undo1.png"))); // NOI18N
        MenuItemEditorUndo.setText("Undo");
        MenuItemEditorUndo.setToolTipText("");
        MenuItemEditorUndo.setEnabled(false);
        MenuItemEditorUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditorUndoActionPerformed();
            }
        });
        ContextMenuEditor.add(MenuItemEditorUndo);

        MenuItemEditorRedo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        MenuItemEditorRedo.setIcon(new ImageIcon(getClass().getResource("/resources/redo1.png"))); // NOI18N
        MenuItemEditorRedo.setText("Redo");
        MenuItemEditorRedo.setToolTipText("");
        MenuItemEditorRedo.setEnabled(false);
        MenuItemEditorRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditorRedoActionPerformed();
            }
        });
        ContextMenuEditor.add(MenuItemEditorRedo);
        ContextMenuEditor.add(editorSeparator1);

        MenuItemEditorSendSelected.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.ALT_MASK));
        MenuItemEditorSendSelected.setIcon(new ImageIcon(getClass().getResource("/resources/send_selected.png"))); // NOI18N
        MenuItemEditorSendSelected.setText("Send selected to ESP");
        MenuItemEditorSendSelected.setToolTipText("Send selected fragment to ESP");
        MenuItemEditorSendSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditorSendSelectedActionPerformed();
            }
        });
        ContextMenuEditor.add(MenuItemEditorSendSelected);

        MenuItemEditorSendLine.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK));
        MenuItemEditorSendLine.setIcon(new ImageIcon(getClass().getResource("/resources/run_line.png"))); // NOI18N
        MenuItemEditorSendLine.setText("Send current line to ESP");
        MenuItemEditorSendLine.setToolTipText("Send current line to ESP");
        MenuItemEditorSendLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditorSendLineActionPerformed();
            }
        });
        ContextMenuEditor.add(MenuItemEditorSendLine);
        ContextMenuEditor.add(editorSeparator);

        MenuItemEditorCut.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditorCut.setIcon(new ImageIcon(getClass().getResource("/resources/cut.png"))); // NOI18N
        MenuItemEditorCut.setText("Cut");
        MenuItemEditorCut.setEnabled(false);
        MenuItemEditorCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditorCutActionPerformed();
            }
        });
        ContextMenuEditor.add(MenuItemEditorCut);

        MenuItemEditorCopy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditorCopy.setIcon(new ImageIcon(getClass().getResource("/resources/copy.png"))); // NOI18N
        MenuItemEditorCopy.setText("Copy");
        MenuItemEditorCopy.setEnabled(false);
        MenuItemEditorCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditorCopyActionPerformed();
            }
        });
        ContextMenuEditor.add(MenuItemEditorCopy);

        MenuItemEditorPaste.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditorPaste.setIcon(new ImageIcon(getClass().getResource("/resources/paste.png"))); // NOI18N
        MenuItemEditorPaste.setText("Paste");
        MenuItemEditorPaste.setEnabled(false);
        MenuItemEditorPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditorPasteActionPerformed();
            }
        });
        ContextMenuEditor.add(MenuItemEditorPaste);
        ContextMenuEditor.add(editorSeparator2);

        menuItemEditorFontInc.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, java.awt.event.InputEvent.ALT_MASK));
        menuItemEditorFontInc.setIcon(new ImageIcon(getClass().getResource("/resources/zoom in.png"))); // NOI18N
        menuItemEditorFontInc.setText("Inc font size");
        menuItemEditorFontInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditorFontIncActionPerformed();
            }
        });
        ContextMenuEditor.add(menuItemEditorFontInc);

        menuItemEditorFontDec.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.ALT_MASK));
        menuItemEditorFontDec.setIcon(new ImageIcon(getClass().getResource("/resources/zoom out.png"))); // NOI18N
        menuItemEditorFontDec.setText("Dec font size");
        menuItemEditorFontDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditorFontDecActionPerformed();
            }
        });
        ContextMenuEditor.add(menuItemEditorFontDec);

        MenuItemLogClear.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemLogClear.setIcon(new ImageIcon(getClass().getResource("/resources/backup_delete.png"))); // NOI18N
        MenuItemLogClear.setText("Clear log");
        MenuItemLogClear.setToolTipText("");
        MenuItemLogClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLogClearActionPerformed();
            }
        });
        contextMenuLog.add(MenuItemLogClear);

        menuItemLogClose.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        menuItemLogClose.setText("Hide Log");
        menuItemLogClose.setToolTipText("");
        menuItemLogClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLogCloseActionPerformed();
            }
        });
        contextMenuLog.add(menuItemLogClose);
        contextMenuLog.add(logSeparator);

        menuItemLogFontInc.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuItemLogFontInc.setIcon(new ImageIcon(getClass().getResource("/resources/zoom in.png"))); // NOI18N
        menuItemLogFontInc.setText("Inc font size");
        menuItemLogFontInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLogFontIncActionPerformed();
            }
        });
        contextMenuLog.add(menuItemLogFontInc);

        menuItemLogFontDec.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuItemLogFontDec.setIcon(new ImageIcon(getClass().getResource("/resources/zoom out.png"))); // NOI18N
        menuItemLogFontDec.setText("Dec font size");
        menuItemLogFontDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLogFontDecActionPerformed();
            }
        });
        contextMenuLog.add(menuItemLogFontDec);

        aboutDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        aboutDialog.setTitle("aboutDialog");
        aboutDialog.setIconImage(null);
        aboutDialog.setMinimumSize(new java.awt.Dimension(406, 250));
        aboutDialog.setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        aboutDialog.setResizable(false);
        aboutDialog.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                AboutFocusLost();
            }
        });

        appName.setFont(new java.awt.Font("Tahoma", Font.BOLD, 24)); // NOI18N
        appName.setHorizontalAlignment(SwingConstants.CENTER);
        appName.setText("ESPlorer");
        appName.setHorizontalTextPosition(SwingConstants.CENTER);

        version1.setHorizontalAlignment(SwingConstants.CENTER);
        version1.setText(Constants.version);
        version1.setHorizontalTextPosition(SwingConstants.CENTER);

        donate.setIcon(new ImageIcon(getClass().getResource("/resources/donate.gif"))); // NOI18N
        donate.setToolTipText("If you'd like to make a one-time donation to ESPlorer author, you can use PayPal to make it fast and easy.");
        donate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DonateActionPerformed();
            }
        });

        author.setHorizontalAlignment(SwingConstants.CENTER);
        author.setText("by h0uz3");
        author.setHorizontalTextPosition(SwingConstants.CENTER);

        homePage.setIcon(new ImageIcon(getClass().getResource("/resources/wifi.png"))); // NOI18N
        homePage.setText("Visit HomePage");
        homePage.setToolTipText("");
        homePage.setHorizontalTextPosition(SwingConstants.LEFT);
        homePage.setMaximumSize(new java.awt.Dimension(200, 55));
        homePage.setMinimumSize(new java.awt.Dimension(200, 55));
        homePage.setPreferredSize(new java.awt.Dimension(200, 55));
        homePage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HomePageActionPerformed();
            }
        });

        GroupLayout AboutLayout = new GroupLayout(aboutDialog.getContentPane());
        aboutDialog.getContentPane().setLayout(AboutLayout);
        AboutLayout.setHorizontalGroup(
                AboutLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(author, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(appName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(version1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(AboutLayout.createSequentialGroup()
                                .addComponent(donate, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(homePage, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                                .addContainerGap())
        );
        AboutLayout.setVerticalGroup(
                AboutLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(AboutLayout.createSequentialGroup()
                                .addContainerGap(77, Short.MAX_VALUE)
                                .addComponent(appName, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(version1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(author)
                                .addGap(56, 56, 56)
                                .addGroup(AboutLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(donate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(homePage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        contextMenuESPFileLUA.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        menuItemESPFileDo.setIcon(new ImageIcon(getClass().getResource("/resources/play.png"))); // NOI18N
        menuItemESPFileDo.setText("Do file");
        menuItemESPFileDo.setToolTipText("");
        menuItemESPFileDo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        contextMenuESPFileLUA.add(menuItemESPFileDo);
        contextMenuESPFileLUA.add(terminalSeparator3);

        menuItemESPFileDelete.setIcon(new ImageIcon(getClass().getResource("/resources/file_remove.png"))); // NOI18N
        menuItemESPFileDelete.setText("Delete file");
        menuItemESPFileDelete.setToolTipText("");
        menuItemESPFileDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        contextMenuESPFileLUA.add(menuItemESPFileDelete);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("ESPlorer");
        setAutoRequestFocus(false);
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFocusCycleRoot(false);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(100, 100));
        setName("ESPlorer"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1024, 768));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained();
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized();
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }

            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
            }

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing();
            }

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened();
            }
        });

        horizontSplit.setDividerLocation(550);
        horizontSplit.setMinimumSize(new java.awt.Dimension(100, 100));
        horizontSplit.setPreferredSize(new java.awt.Dimension(768, 567));

        LeftBasePane.setMinimumSize(new java.awt.Dimension(100, 100));

        LeftTab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        LeftTab.setToolTipText("");
        LeftTab.setAlignmentX(0.0F);
        LeftTab.setAlignmentY(0.0F);
        LeftTab.setAutoscrolls(true);
        LeftTab.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        LeftTab.setMinimumSize(new java.awt.Dimension(100, 100));
        LeftTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                LeftTabStateChanged();
            }
        });

        nodeMCU.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        nodeMCU.setMinimumSize(new java.awt.Dimension(100, 100));
        nodeMCU.setPreferredSize(new java.awt.Dimension(461, 537));
        nodeMCU.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
            }
        });
        nodeMCU.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                NodeMCUComponentShown();
            }
        });

        TextTab.setBorder(BorderFactory.createEtchedBorder());
        TextTab.setComponentPopupMenu(ContextMenuEditor);
        TextTab.setMinimumSize(new java.awt.Dimension(462, 365));
        TextTab.setPreferredSize(new java.awt.Dimension(462, 365));
        TextTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
            }
        });

        sriptsTab.setToolTipText("");
        sriptsTab.setMinimumSize(new java.awt.Dimension(460, 350));

        FilesToolBar.setFloatable(false);
        FilesToolBar.setRollover(true);
        FilesToolBar.setAlignmentY(0.5F);
        FilesToolBar.setMaximumSize(new java.awt.Dimension(1000, 40));
        FilesToolBar.setMinimumSize(new java.awt.Dimension(321, 40));
        FilesToolBar.setPreferredSize(new java.awt.Dimension(321, 40));

        ButtonFileNew.setAction(MenuItemFileNew.getAction());
        ButtonFileNew.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonFileNew.setIcon(new ImageIcon(getClass().getResource("/resources/document.png"))); // NOI18N
        ButtonFileNew.setToolTipText("New file");
        ButtonFileNew.setText("New");
        ButtonFileNew.setHorizontalTextPosition(SwingConstants.CENTER);
        ButtonFileNew.setMaximumSize(new java.awt.Dimension(40, 40));
        ButtonFileNew.setMinimumSize(new java.awt.Dimension(40, 40));
        ButtonFileNew.setPreferredSize(new java.awt.Dimension(40, 40));
        ButtonFileNew.setVerticalTextPosition(SwingConstants.BOTTOM);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, MenuItemFileNew, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), ButtonFileNew, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        ButtonFileNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonFileNewActionPerformed();
            }
        });
        FilesToolBar.add(ButtonFileNew);

        buttonFileOpen.setAction(MenuItemFileOpen.getAction());
        buttonFileOpen.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        buttonFileOpen.setIcon(new ImageIcon(getClass().getResource("/resources/folder open.png"))); // NOI18N
        buttonFileOpen.setText("Open");
        buttonFileOpen.setToolTipText("Open file from disk");
        buttonFileOpen.setHideActionText(true);
        buttonFileOpen.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonFileOpen.setMaximumSize(new java.awt.Dimension(40, 40));
        buttonFileOpen.setMinimumSize(new java.awt.Dimension(40, 40));
        buttonFileOpen.setPreferredSize(new java.awt.Dimension(40, 40));
        buttonFileOpen.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonFileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonFileOpenActionPerformed();
            }
        });
        FilesToolBar.add(buttonFileOpen);

        ButtonFileReload.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonFileReload.setIcon(new ImageIcon(getClass().getResource("/resources/refresh.png"))); // NOI18N
        ButtonFileReload.setText("Reload");
        ButtonFileReload.setToolTipText("Reload file from disk (for use with external editor)");
        ButtonFileReload.setHorizontalTextPosition(SwingConstants.CENTER);
        ButtonFileReload.setMaximumSize(new java.awt.Dimension(40, 40));
        ButtonFileReload.setMinimumSize(new java.awt.Dimension(40, 40));
        ButtonFileReload.setPreferredSize(new java.awt.Dimension(40, 40));
        ButtonFileReload.setVerticalTextPosition(SwingConstants.BOTTOM);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, MenuItemFileReload, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), ButtonFileReload, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        ButtonFileReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonFileReloadActionPerformed();
            }
        });
        FilesToolBar.add(ButtonFileReload);

        ButtonFileSave.setAction(MenuItemFileSave.getAction());
        ButtonFileSave.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonFileSave.setIcon(new ImageIcon(getClass().getResource("/resources/save.png"))); // NOI18N
        ButtonFileSave.setText("Save");
        ButtonFileSave.setToolTipText("Save file to disk");
        ButtonFileSave.setHideActionText(true);
        ButtonFileSave.setHorizontalTextPosition(SwingConstants.CENTER);
        ButtonFileSave.setMaximumSize(new java.awt.Dimension(40, 40));
        ButtonFileSave.setMinimumSize(new java.awt.Dimension(40, 40));
        ButtonFileSave.setPreferredSize(new java.awt.Dimension(40, 40));
        ButtonFileSave.setVerticalTextPosition(SwingConstants.BOTTOM);
        ButtonFileSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonFileSaveActionPerformed();
            }
        });
        FilesToolBar.add(ButtonFileSave);

        buttonFileSaveAll.setAction(MenuItemFileSave.getAction());
        buttonFileSaveAll.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        buttonFileSaveAll.setIcon(new ImageIcon(getClass().getResource("/resources/save_all.png"))); // NOI18N
        buttonFileSaveAll.setText("SaveAll");
        buttonFileSaveAll.setToolTipText("Save all open files to disk");
        buttonFileSaveAll.setEnabled(false);
        buttonFileSaveAll.setFocusable(false);
        buttonFileSaveAll.setHideActionText(true);
        buttonFileSaveAll.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonFileSaveAll.setMaximumSize(new java.awt.Dimension(40, 40));
        buttonFileSaveAll.setMinimumSize(new java.awt.Dimension(40, 40));
        buttonFileSaveAll.setPreferredSize(new java.awt.Dimension(40, 40));
        buttonFileSaveAll.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonFileSaveAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonFileSaveAllActionPerformed();
            }
        });
        FilesToolBar.add(buttonFileSaveAll);

        ButtonFileClose.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonFileClose.setIcon(new ImageIcon(getClass().getResource("/resources/folder closed.png"))); // NOI18N
        ButtonFileClose.setText("Close");
        ButtonFileClose.setToolTipText("Close file");
        ButtonFileClose.setHideActionText(true);
        ButtonFileClose.setHorizontalTextPosition(SwingConstants.CENTER);
        ButtonFileClose.setMaximumSize(new java.awt.Dimension(40, 40));
        ButtonFileClose.setMinimumSize(new java.awt.Dimension(40, 40));
        ButtonFileClose.setPreferredSize(new java.awt.Dimension(40, 40));
        ButtonFileClose.setVerticalTextPosition(SwingConstants.BOTTOM);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, MenuItemFileClose, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), ButtonFileClose, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        ButtonFileClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonFileCloseActionPerformed();
            }
        });
        FilesToolBar.add(ButtonFileClose);
        FilesToolBar.add(jSeparator1);

        ButtonUndo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonUndo.setIcon(new ImageIcon(getClass().getResource("/resources/undo1.png"))); // NOI18N
        ButtonUndo.setText("Undo");
        ButtonUndo.setToolTipText("Undo last action");
        ButtonUndo.setEnabled(false);
        ButtonUndo.setFocusable(false);
        ButtonUndo.setHideActionText(true);
        ButtonUndo.setHorizontalTextPosition(SwingConstants.CENTER);
        ButtonUndo.setMaximumSize(new java.awt.Dimension(40, 40));
        ButtonUndo.setMinimumSize(new java.awt.Dimension(40, 40));
        ButtonUndo.setPreferredSize(new java.awt.Dimension(40, 40));
        ButtonUndo.setVerticalTextPosition(SwingConstants.BOTTOM);
        ButtonUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonUndoActionPerformed();
            }
        });
        FilesToolBar.add(ButtonUndo);

        ButtonRedo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonRedo.setIcon(new ImageIcon(getClass().getResource("/resources/redo1.png"))); // NOI18N
        ButtonRedo.setText("Redo");
        ButtonRedo.setToolTipText("Redo last action");
        ButtonRedo.setEnabled(false);
        ButtonRedo.setFocusable(false);
        ButtonRedo.setHideActionText(true);
        ButtonRedo.setHorizontalTextPosition(SwingConstants.CENTER);
        ButtonRedo.setMaximumSize(new java.awt.Dimension(40, 40));
        ButtonRedo.setMinimumSize(new java.awt.Dimension(40, 40));
        ButtonRedo.setPreferredSize(new java.awt.Dimension(40, 40));
        ButtonRedo.setVerticalTextPosition(SwingConstants.BOTTOM);
        ButtonRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonRedoActionPerformed();
            }
        });
        FilesToolBar.add(ButtonRedo);
        FilesToolBar.add(jSeparator8);

        ButtonCut.setAction(MenuItemEditCut.getAction());
        ButtonCut.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonCut.setIcon(new ImageIcon(getClass().getResource("/resources/cut.png"))); // NOI18N
        ButtonCut.setText("Cut");
        ButtonCut.setToolTipText("Cut");
        ButtonCut.setEnabled(false);
        ButtonCut.setHideActionText(true);
        ButtonCut.setHorizontalTextPosition(SwingConstants.CENTER);
        ButtonCut.setMaximumSize(new java.awt.Dimension(40, 40));
        ButtonCut.setMinimumSize(new java.awt.Dimension(40, 40));
        ButtonCut.setPreferredSize(new java.awt.Dimension(40, 40));
        ButtonCut.setVerticalTextPosition(SwingConstants.BOTTOM);
        ButtonCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCutActionPerformed();
            }
        });
        FilesToolBar.add(ButtonCut);

        copyButton.setAction(MenuItemEditCopy.getAction());
        copyButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        copyButton.setIcon(new ImageIcon(getClass().getResource("/resources/copy.png"))); // NOI18N
        copyButton.setText("Copy");
        copyButton.setToolTipText("Copy");
        copyButton.setEnabled(false);
        copyButton.setHideActionText(true);
        copyButton.setHorizontalTextPosition(SwingConstants.CENTER);
        copyButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        copyButton.setMaximumSize(new java.awt.Dimension(40, 40));
        copyButton.setMinimumSize(new java.awt.Dimension(40, 40));
        copyButton.setPreferredSize(new java.awt.Dimension(40, 40));
        copyButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCopyActionPerformed();
            }
        });
        FilesToolBar.add(copyButton);

        ButtonPaste.setAction(MenuItemEditPaste.getAction());
        ButtonPaste.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonPaste.setIcon(new ImageIcon(getClass().getResource("/resources/paste.png"))); // NOI18N
        ButtonPaste.setText("Paste");
        ButtonPaste.setToolTipText("Paste");
        ButtonPaste.setEnabled(false);
        ButtonPaste.setHideActionText(true);
        ButtonPaste.setHorizontalTextPosition(SwingConstants.CENTER);
        ButtonPaste.setMaximumSize(new java.awt.Dimension(40, 40));
        ButtonPaste.setMinimumSize(new java.awt.Dimension(40, 40));
        ButtonPaste.setPreferredSize(new java.awt.Dimension(40, 40));
        ButtonPaste.setVerticalTextPosition(SwingConstants.BOTTOM);
        ButtonPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPasteActionPerformed();
            }
        });
        FilesToolBar.add(ButtonPaste);
        FilesToolBar.add(jSeparator6);

        ButtonSendSelected.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSendSelected.setIcon(new ImageIcon(getClass().getResource("/resources/send_selected.png"))); // NOI18N
        ButtonSendSelected.setToolTipText("Send selected block to ESP");
        ButtonSendSelected.setHideActionText(true);
        ButtonSendSelected.setHorizontalTextPosition(SwingConstants.CENTER);
        ButtonSendSelected.setText("Block");
        ButtonSendSelected.setMaximumSize(new java.awt.Dimension(40, 40));
        ButtonSendSelected.setMinimumSize(new java.awt.Dimension(40, 40));
        ButtonSendSelected.setPreferredSize(new java.awt.Dimension(40, 40));
        ButtonSendSelected.setVerticalTextPosition(SwingConstants.BOTTOM);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, MenuItemEditorSendSelected, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), ButtonSendSelected, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        ButtonSendSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSendSelectedActionPerformed();
            }
        });
        FilesToolBar.add(ButtonSendSelected);

        ButtonSendLine.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSendLine.setIcon(new ImageIcon(getClass().getResource("/resources/run_line.png"))); // NOI18N
        ButtonSendLine.setText("Line");
        ButtonSendLine.setToolTipText("Send current line to ESP");
        ButtonSendLine.setFocusable(false);
        ButtonSendLine.setHideActionText(true);
        ButtonSendLine.setHorizontalTextPosition(SwingConstants.CENTER);
        ButtonSendLine.setMaximumSize(new java.awt.Dimension(40, 40));
        ButtonSendLine.setMinimumSize(new java.awt.Dimension(40, 40));
        ButtonSendLine.setPreferredSize(new java.awt.Dimension(40, 40));
        ButtonSendLine.setVerticalTextPosition(SwingConstants.BOTTOM);
        ButtonSendLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSendLineActionPerformed();
            }
        });
        FilesToolBar.add(ButtonSendLine);

        FilesTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FilesTabbedPaneStateChanged();
            }
        });

        fileLayeredPane.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
            }
        });

        textScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        textScroll.setViewportBorder(BorderFactory.createEtchedBorder());
        textScroll.setFoldIndicatorEnabled(true);
        textScroll.setLineNumbersEnabled(true);
        textScroll.setViewportView(textEditor);

        textEditor.setColumns(20);
        textEditor.setRows(5);
        textEditor.setTabSize(4);
        textEditor.setCodeFoldingEnabled(true);
        textEditor.setDragEnabled(false);
        textEditor.setFadeCurrentLineHighlight(true);
        textEditor.setPaintMarkOccurrencesBorder(true);
        textEditor.setPaintMatchedBracketPair(true);
        textEditor.setPopupMenu(ContextMenuEditor);
        textEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
        textEditor.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                TextEditorCaretUpdate();
            }
        });
        textEditor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
            }
        });
        textEditor.addActiveLineRangeListener(new org.fife.ui.rsyntaxtextarea.ActiveLineRangeListener() {
            public void activeLineRangeChanged(org.fife.ui.rsyntaxtextarea.ActiveLineRangeEvent evt) {
                TextEditorActiveLineRangeChanged();
            }
        });
        textEditor.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                TextEditorCaretPositionChanged();
            }

            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        textEditor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextEditorKeyTyped();
            }
        });
        textScroll.setViewportView(textEditor);

        GroupLayout FileLayeredPaneLayout = new GroupLayout(fileLayeredPane);
        fileLayeredPane.setLayout(FileLayeredPaneLayout);
        FileLayeredPaneLayout.setHorizontalGroup(
                FileLayeredPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(textScroll, GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
        );
        FileLayeredPaneLayout.setVerticalGroup(
                FileLayeredPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(textScroll, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
        );
        fileLayeredPane.setLayer(textScroll, JLayeredPane.DEFAULT_LAYER);

        FilesTabbedPane.addTab("NewFile", fileLayeredPane);

        LeftExtraButtons.setEnabled(false);
        LeftExtraButtons.setOpaque(true);
        LeftExtraButtons.setPreferredSize(new java.awt.Dimension(431, 66));
        LeftExtraButtons.setLayout(new java.awt.FlowLayout());

        fileDo1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        fileDo1.setText("Save&Run");
        fileDo1.setToolTipText("Do lua script");
        fileDo1.setHorizontalTextPosition(SwingConstants.CENTER);
        fileDo1.setIconTextGap(8);
        fileDo1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        fileDo1.setMaximumSize(new java.awt.Dimension(127, 30));
        fileDo1.setMinimumSize(new java.awt.Dimension(127, 30));
        fileDo1.setPreferredSize(new java.awt.Dimension(127, 30));
        fileDo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        LeftExtraButtons.add(fileDo1);

        fileCompile.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        fileCompile.setText("Save&Compile");
        fileCompile.setToolTipText("Recompile lua script and do lua bytecode lc file");
        fileCompile.setIconTextGap(2);
        fileCompile.setMargin(new java.awt.Insets(2, 2, 2, 2));
        fileCompile.setMaximumSize(new java.awt.Dimension(127, 30));
        fileCompile.setMinimumSize(new java.awt.Dimension(127, 30));
        fileCompile.setPreferredSize(new java.awt.Dimension(127, 30));
        fileCompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileCompileActionPerformed();
            }
        });
        LeftExtraButtons.add(fileCompile);

        fileSaveCompileDoLC.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        fileSaveCompileDoLC.setText("Save&Compile&RunLC");
        fileSaveCompileDoLC.setIconTextGap(0);
        fileSaveCompileDoLC.setMargin(new java.awt.Insets(2, 0, 2, 0));
        fileSaveCompileDoLC.setMaximumSize(new java.awt.Dimension(127, 30));
        fileSaveCompileDoLC.setMinimumSize(new java.awt.Dimension(127, 30));
        fileSaveCompileDoLC.setPreferredSize(new java.awt.Dimension(127, 30));
        fileSaveCompileDoLC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        LeftExtraButtons.add(fileSaveCompileDoLC);

        fileCompileDoLC.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        fileCompileDoLC.setText("Save&Compile All");
        fileCompileDoLC.setActionCommand("Save&CompileAll");
        fileCompileDoLC.setHorizontalTextPosition(SwingConstants.CENTER);
        fileCompileDoLC.setIconTextGap(0);
        fileCompileDoLC.setMargin(new java.awt.Insets(2, 0, 2, 0));
        fileCompileDoLC.setMaximumSize(new java.awt.Dimension(127, 30));
        fileCompileDoLC.setMinimumSize(new java.awt.Dimension(127, 30));
        fileCompileDoLC.setPreferredSize(new java.awt.Dimension(127, 30));
        fileCompileDoLC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileCompileDoLCActionPerformed();
            }
        });
        LeftExtraButtons.add(fileCompileDoLC);

        fileCompile1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        fileCompile1.setText("Save&Compile");
        fileCompile1.setToolTipText("Repompile lua script and do lua bytecode lc file");
        fileCompile1.setIconTextGap(2);
        fileCompile1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        fileCompile1.setMaximumSize(new java.awt.Dimension(127, 30));
        fileCompile1.setMinimumSize(new java.awt.Dimension(127, 30));
        fileCompile1.setPreferredSize(new java.awt.Dimension(127, 30));
        fileCompile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        LeftExtraButtons.add(fileCompile1);

        Busy.setBackground(new java.awt.Color(0, 153, 0));
        Busy.setForeground(new java.awt.Color(255, 255, 255));
        Busy.setHorizontalAlignment(SwingConstants.CENTER);
        Busy.setIcon(new ImageIcon(getClass().getResource("/resources/led_grey.png"))); // NOI18N
        Busy.setText("IDLE");
        Busy.setOpaque(true);

        FilePathLabel.setText("jLabel1");

        ProgressBar.setToolTipText("");
        ProgressBar.setOpaque(true);
        ProgressBar.setStringPainted(true);

        leftMainButtons.setLayout(new java.awt.FlowLayout());

        FileSaveESP.setIcon(new ImageIcon(getClass().getResource("/resources/move.png"))); // NOI18N
        FileSaveESP.setText("<html><u>S</u>ave to ESP");
        FileSaveESP.setToolTipText("Send file to ESP and save into flash memory");
        FileSaveESP.setHorizontalAlignment(SwingConstants.LEFT);
        FileSaveESP.setIconTextGap(8);
        FileSaveESP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileSaveESP.setMaximumSize(new java.awt.Dimension(127, 30));
        FileSaveESP.setMinimumSize(new java.awt.Dimension(127, 30));
        FileSaveESP.setPreferredSize(new java.awt.Dimension(127, 30));
        FileSaveESP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileSaveESPActionPerformed();
            }
        });
        leftMainButtons.add(FileSaveESP);

        FileSendESP.setIcon(new ImageIcon(getClass().getResource("/resources/script_send.png"))); // NOI18N
        FileSendESP.setText("<html>S<u>e</u>nd to ESP");
        FileSendESP.setToolTipText("Send file to ESP and run  \"line by line\"");
        FileSendESP.setHorizontalAlignment(SwingConstants.LEFT);
        FileSendESP.setIconTextGap(8);
        FileSendESP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileSendESP.setMaximumSize(new java.awt.Dimension(127, 30));
        FileSendESP.setMinimumSize(new java.awt.Dimension(127, 30));
        FileSendESP.setPreferredSize(new java.awt.Dimension(127, 30));
        FileSendESP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileSendESPActionPerformed();
            }
        });
        leftMainButtons.add(FileSendESP);

        FileDo.setIcon(new ImageIcon(getClass().getResource("/resources/play.png"))); // NOI18N
        FileDo.setText("Run");
        FileDo.setToolTipText("Execute lua script via \"dofile\" command");
        FileDo.setHorizontalAlignment(SwingConstants.LEFT);
        FileDo.setIconTextGap(8);
        FileDo.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileDo.setMaximumSize(new java.awt.Dimension(127, 30));
        FileDo.setMinimumSize(new java.awt.Dimension(127, 30));
        FileDo.setPreferredSize(new java.awt.Dimension(127, 30));
        FileDo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileDoActionPerformed();
            }
        });
        leftMainButtons.add(FileDo);

        // TODO/FIXME FilesUpload.setIcon(new ImageIcon(getClass().getResource("/resources/uploadLUA.png"))); // NOI18N
        filesUpload.setText("Upload ...");
        filesUpload.setToolTipText("Upload file from disk to ESP flash memory");
        filesUpload.setHorizontalAlignment(SwingConstants.LEFT);
        filesUpload.setIconTextGap(8);
        filesUpload.setMargin(new java.awt.Insets(2, 2, 2, 2));
        filesUpload.setMaximumSize(new java.awt.Dimension(127, 30));
        filesUpload.setMinimumSize(new java.awt.Dimension(127, 30));
        filesUpload.setPreferredSize(new java.awt.Dimension(127, 30));
        filesUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilesUploadActionPerformed();
            }
        });
        leftMainButtons.add(filesUpload);

        GroupLayout SriptsTabLayout = new GroupLayout(sriptsTab);
        sriptsTab.setLayout(SriptsTabLayout);
        SriptsTabLayout.setHorizontalGroup(
                SriptsTabLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(FilesTabbedPane)
                        .addComponent(FilesToolBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(SriptsTabLayout.createSequentialGroup()
                                .addGroup(SriptsTabLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(ProgressBar, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.LEADING, SriptsTabLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(Busy, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(FilePathLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(LeftExtraButtons, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 531, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(leftMainButtons, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 531, GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        SriptsTabLayout.setVerticalGroup(
                SriptsTabLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(SriptsTabLayout.createSequentialGroup()
                                .addComponent(FilesToolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FilesTabbedPane)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ProgressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(SriptsTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(Busy)
                                        .addComponent(FilePathLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LeftExtraButtons, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(leftMainButtons, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        sriptsTab.setLayer(FilesToolBar, JLayeredPane.PALETTE_LAYER);
        sriptsTab.setLayer(FilesTabbedPane, JLayeredPane.DEFAULT_LAYER);
        sriptsTab.setLayer(LeftExtraButtons, JLayeredPane.DEFAULT_LAYER);
        sriptsTab.setLayer(Busy, JLayeredPane.DEFAULT_LAYER);
        sriptsTab.setLayer(FilePathLabel, JLayeredPane.DEFAULT_LAYER);
        sriptsTab.setLayer(ProgressBar, JLayeredPane.DEFAULT_LAYER);
        sriptsTab.setLayer(leftMainButtons, JLayeredPane.DEFAULT_LAYER);

        TextTab.addTab("Scripts", sriptsTab);
        sriptsTab.getAccessibleContext().setAccessibleName("Files");

        nodeMCUCommands.setOpaque(true);

        cmdNodeRestart.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdNodeRestart.setText("Restart ESP");
        cmdNodeRestart.setToolTipText("");
        cmdNodeRestart.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdNodeRestart.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdNodeRestart.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdNodeRestart.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdNodeRestart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNodeRestartActionPerformed();
            }
        });

        cmdNodeChipID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdNodeChipID.setText("Chip ID");
        cmdNodeChipID.setToolTipText("");
        cmdNodeChipID.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdNodeChipID.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdNodeChipID.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdNodeChipID.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdNodeChipID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNodeChipIDActionPerformed();
            }
        });

        cmdNodeHeap.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdNodeHeap.setText("Heap");
        cmdNodeHeap.setToolTipText("");
        cmdNodeHeap.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdNodeHeap.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdNodeHeap.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdNodeHeap.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdNodeHeap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNodeHeapActionPerformed();
            }
        });

        cmdNodeSleep.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdNodeSleep.setText("Sleep 10000");
        cmdNodeSleep.setToolTipText("");
        cmdNodeSleep.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdNodeSleep.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdNodeSleep.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdNodeSleep.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdNodeSleep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNodeSleepActionPerformed();
            }
        });

        cmdListFiles.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdListFiles.setText("List files");
        cmdListFiles.setToolTipText("");
        cmdListFiles.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdListFiles.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdListFiles.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdListFiles.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdListFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdListFilesActionPerformed();
            }
        });

        cmdTimerStop.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdTimerStop.setText("tmr.stop");
        cmdTimerStop.setToolTipText("");
        cmdTimerStop.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdTimerStop.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdTimerStop.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdTimerStop.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdTimerStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTimerStopActionPerformed();
            }
        });

        TimerNumber.setMaximumRowCount(7);
        TimerNumber.setModel(new DefaultComboBoxModel(new String[]{"0", "1", "2", "3", "4", "5", "6"}));
        TimerNumber.setToolTipText("Timer number (0-6)");

        jLayeredPane1.setBorder(BorderFactory.createTitledBorder("GPIO"));

        GroupLayout jLayeredPane1Layout = new GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
                jLayeredPane1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
                jLayeredPane1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 177, Short.MAX_VALUE)
        );

        GroupLayout NodeMCUCommandsLayout = new GroupLayout(nodeMCUCommands);
        nodeMCUCommands.setLayout(NodeMCUCommandsLayout);
        NodeMCUCommandsLayout.setHorizontalGroup(
                NodeMCUCommandsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(NodeMCUCommandsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(NodeMCUCommandsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLayeredPane1)
                                        .addGroup(NodeMCUCommandsLayout.createSequentialGroup()
                                                .addGroup(NodeMCUCommandsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(NodeMCUCommandsLayout.createSequentialGroup()
                                                                .addComponent(cmdNodeRestart, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(cmdNodeChipID, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(NodeMCUCommandsLayout.createSequentialGroup()
                                                                .addComponent(cmdTimerStop, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(TimerNumber, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(NodeMCUCommandsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(NodeMCUCommandsLayout.createSequentialGroup()
                                                                .addComponent(cmdNodeHeap, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(cmdNodeSleep, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(cmdListFiles, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        NodeMCUCommandsLayout.setVerticalGroup(
                NodeMCUCommandsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(NodeMCUCommandsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(NodeMCUCommandsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(cmdNodeChipID, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdNodeRestart, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdNodeHeap, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdNodeSleep, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(NodeMCUCommandsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(cmdListFiles, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdTimerStop, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(TimerNumber, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLayeredPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(250, 250, 250))
        );
        nodeMCUCommands.setLayer(cmdNodeRestart, JLayeredPane.DEFAULT_LAYER);
        nodeMCUCommands.setLayer(cmdNodeChipID, JLayeredPane.DEFAULT_LAYER);
        nodeMCUCommands.setLayer(cmdNodeHeap, JLayeredPane.DEFAULT_LAYER);
        nodeMCUCommands.setLayer(cmdNodeSleep, JLayeredPane.DEFAULT_LAYER);
        nodeMCUCommands.setLayer(cmdListFiles, JLayeredPane.DEFAULT_LAYER);
        nodeMCUCommands.setLayer(cmdTimerStop, JLayeredPane.DEFAULT_LAYER);
        nodeMCUCommands.setLayer(TimerNumber, JLayeredPane.DEFAULT_LAYER);
        nodeMCUCommands.setLayer(jLayeredPane1, JLayeredPane.DEFAULT_LAYER);

        TextTab.addTab("Commands", nodeMCUCommands);

        leftSnippetsPane.setOpaque(true);

        SnippetEdit0.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit0.setText("Edit Snippet0");
        SnippetEdit0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit0ActionPerformed();
            }
        });

        SnippetEdit1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit1.setText("Edit Snippet1");
        SnippetEdit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit1ActionPerformed();
            }
        });

        SnippetEdit2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit2.setText("Edit Snippet2");
        SnippetEdit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit2ActionPerformed();
            }
        });

        SnippetEdit3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit3.setText("Edit Snippet3");
        SnippetEdit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit3ActionPerformed();
            }
        });

        SnippetEdit4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit4.setText("Edit Snippet4");
        SnippetEdit4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit4ActionPerformed();
            }
        });

        SnippetEdit5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit5.setText("Edit Snippet5");
        SnippetEdit5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit5ActionPerformed();
            }
        });

        SnippetEdit6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit6.setText("Edit Snippet6");
        SnippetEdit6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit6ActionPerformed();
            }
        });

        SnippetEdit7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit7.setText("Edit Snippet7");
        SnippetEdit7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit7ActionPerformed();
            }
        });

        SnippetEdit8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit8.setText("Edit Snippet8");
        SnippetEdit8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit8ActionPerformed();
            }
        });

        SnippetEdit9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit9.setText("Edit Snippet9");
        SnippetEdit9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit9ActionPerformed();
            }
        });

        SnippetEdit10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit10.setText("Edit Snippet10");
        SnippetEdit10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit10ActionPerformed();
            }
        });

        SnippetEdit11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit11.setText("Edit Snippet11");
        SnippetEdit11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit11ActionPerformed();
            }
        });

        snippetEdit12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        snippetEdit12.setText("Edit Snippet12");
        snippetEdit12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit12ActionPerformed();
            }
        });

        SnippetEdit13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit13.setText("Edit Snippet13");
        SnippetEdit13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit13ActionPerformed();
            }
        });

        SnippetEdit14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit14.setText("Edit Snippet14");
        SnippetEdit14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit14ActionPerformed();
            }
        });

        SnippetEdit15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetEdit15.setText("Edit Snippet15");
        SnippetEdit15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetEdit15ActionPerformed();
            }
        });

        GroupLayout LeftSnippetsPaneLayout = new GroupLayout(leftSnippetsPane);
        leftSnippetsPane.setLayout(LeftSnippetsPaneLayout);
        LeftSnippetsPaneLayout.setHorizontalGroup(
                LeftSnippetsPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(SnippetEdit0, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit3, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit4, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit5, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit6, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit7, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit8, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit9, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit10, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                        .addComponent(SnippetEdit11, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(snippetEdit12, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit13, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit14, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SnippetEdit15, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        LeftSnippetsPaneLayout.setVerticalGroup(
                LeftSnippetsPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LeftSnippetsPaneLayout.createSequentialGroup()
                                .addComponent(SnippetEdit0, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit1, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit2, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit3, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit4, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit5, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit6, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit7, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit8, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit9, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit10, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit11, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(snippetEdit12, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit13, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit14, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetEdit15, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(252, Short.MAX_VALUE))
        );
        leftSnippetsPane.setLayer(SnippetEdit0, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit1, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit2, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit3, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit4, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit5, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit6, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit7, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit8, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit9, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit10, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit11, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(snippetEdit12, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit13, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit14, JLayeredPane.DEFAULT_LAYER);
        leftSnippetsPane.setLayer(SnippetEdit15, JLayeredPane.DEFAULT_LAYER);

        SnippetTopPane.setOpaque(true);

        SnippetName.setEnabled(false);

        SnippetSave.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetSave.setIcon(new ImageIcon(getClass().getResource("/resources/save.png"))); // NOI18N
        SnippetSave.setText("Save");
        SnippetSave.setToolTipText("Save snippet to disk");
        SnippetSave.setEnabled(false);
        SnippetSave.setMargin(new java.awt.Insets(2, 2, 2, 2));
        SnippetSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetSaveActionPerformed();
            }
        });

        SnippetRun.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetRun.setIcon(new ImageIcon(getClass().getResource("/resources/server1_start.png"))); // NOI18N
        SnippetRun.setText("Run");
        SnippetRun.setToolTipText("Run snippet");
        SnippetRun.setEnabled(false);
        SnippetRun.setMargin(new java.awt.Insets(2, 2, 2, 2));
        SnippetRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetRunActionPerformed();
            }
        });

        SnippetsBusy.setBackground(new java.awt.Color(0, 153, 0));
        SnippetsBusy.setForeground(new java.awt.Color(255, 255, 255));
        SnippetsBusy.setHorizontalAlignment(SwingConstants.CENTER);
        SnippetsBusy.setIcon(new ImageIcon(getClass().getResource("/resources/led_grey.png"))); // NOI18N
        SnippetsBusy.setText("IDLE");
        SnippetsBusy.setOpaque(true);

        SnippetCancelEdit.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SnippetCancelEdit.setIcon(new ImageIcon(getClass().getResource("/resources/abort.png"))); // NOI18N
        SnippetCancelEdit.setText("Cancel");
        SnippetCancelEdit.setToolTipText("Cancel edit and clear editor window");
        SnippetCancelEdit.setEnabled(false);
        SnippetCancelEdit.setMargin(new java.awt.Insets(2, 2, 2, 2));
        SnippetCancelEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnippetCancelEditActionPerformed();
            }
        });

        Condensed.setText("Condensed executing");
        Condensed.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CondensedItemStateChanged();
            }
        });

        GroupLayout SnippetTopPaneLayout = new GroupLayout(SnippetTopPane);
        SnippetTopPane.setLayout(SnippetTopPaneLayout);
        SnippetTopPaneLayout.setHorizontalGroup(
                SnippetTopPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(SnippetTopPaneLayout.createSequentialGroup()
                                .addGroup(SnippetTopPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(SnippetTopPaneLayout.createSequentialGroup()
                                                .addComponent(SnippetSave)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(SnippetCancelEdit))
                                        .addComponent(SnippetName))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(SnippetTopPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(SnippetsBusy, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(SnippetTopPaneLayout.createSequentialGroup()
                                                .addComponent(SnippetRun, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Condensed)))
                                .addContainerGap(69, Short.MAX_VALUE))
        );
        SnippetTopPaneLayout.setVerticalGroup(
                SnippetTopPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(SnippetTopPaneLayout.createSequentialGroup()
                                .addGroup(SnippetTopPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(SnippetName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(SnippetsBusy))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(SnippetTopPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(SnippetSave)
                                        .addComponent(SnippetRun)
                                        .addComponent(SnippetCancelEdit)
                                        .addComponent(Condensed)))
        );
        SnippetTopPane.setLayer(SnippetName, JLayeredPane.DEFAULT_LAYER);
        SnippetTopPane.setLayer(SnippetSave, JLayeredPane.DEFAULT_LAYER);
        SnippetTopPane.setLayer(SnippetRun, JLayeredPane.DEFAULT_LAYER);
        SnippetTopPane.setLayer(SnippetsBusy, JLayeredPane.DEFAULT_LAYER);
        SnippetTopPane.setLayer(SnippetCancelEdit, JLayeredPane.DEFAULT_LAYER);
        SnippetTopPane.setLayer(Condensed, JLayeredPane.DEFAULT_LAYER);

        SnippetScrollPane.setBorder(BorderFactory.createEtchedBorder());
        SnippetScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        SnippetScrollPane.setEnabled(false);
        SnippetScrollPane.setLineNumbersEnabled(true);

        SnippetText.setEditable(false);
        SnippetText.setColumns(20);
        SnippetText.setRows(5);
        SnippetText.setEnabled(false);
        SnippetScrollPane.setViewportView(SnippetText);

        GroupLayout NodeMCUSnippetsLayout = new GroupLayout(nodeMCUSnippets);
        nodeMCUSnippets.setLayout(NodeMCUSnippetsLayout);
        NodeMCUSnippetsLayout.setHorizontalGroup(
                NodeMCUSnippetsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(NodeMCUSnippetsLayout.createSequentialGroup()
                                .addComponent(leftSnippetsPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(NodeMCUSnippetsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(SnippetTopPane)
                                        .addComponent(SnippetScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        NodeMCUSnippetsLayout.setVerticalGroup(
                NodeMCUSnippetsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(NodeMCUSnippetsLayout.createSequentialGroup()
                                .addComponent(SnippetTopPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SnippetScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(leftSnippetsPane)
        );
        nodeMCUSnippets.setLayer(leftSnippetsPane, JLayeredPane.DEFAULT_LAYER);
        nodeMCUSnippets.setLayer(SnippetTopPane, JLayeredPane.DEFAULT_LAYER);
        nodeMCUSnippets.setLayer(SnippetScrollPane, JLayeredPane.DEFAULT_LAYER);

        TextTab.addTab("Snippets", nodeMCUSnippets);

        nodeMCUSettings.setAutoscrolls(true);
        nodeMCUSettings.setOpaque(true);

        optionsFirmware.setBorder(BorderFactory.createTitledBorder(null, "Select firmware", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N
        optionsFirmware.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        firmware.add(OptionNodeMCU);
        OptionNodeMCU.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        OptionNodeMCU.setSelected(true);
        OptionNodeMCU.setText("NodeMCU");
        OptionNodeMCU.setToolTipText("");
        OptionNodeMCU.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OptionNodeMCUItemStateChanged();
            }
        });

        firmware.add(OptionMicroPython);
        OptionMicroPython.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        OptionMicroPython.setText("MicroPython");
        OptionMicroPython.setEnabled(false);
        OptionMicroPython.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OptionMicroPythonItemStateChanged();
            }
        });

        GroupLayout OptionsFirmwareLayout = new GroupLayout(optionsFirmware);
        optionsFirmware.setLayout(OptionsFirmwareLayout);
        OptionsFirmwareLayout.setHorizontalGroup(
                OptionsFirmwareLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(OptionNodeMCU, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(OptionMicroPython, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        OptionsFirmwareLayout.setVerticalGroup(
                OptionsFirmwareLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(OptionsFirmwareLayout.createSequentialGroup()
                                .addComponent(OptionNodeMCU)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(OptionMicroPython)
                                .addContainerGap())
        );
        optionsFirmware.setLayer(OptionNodeMCU, JLayeredPane.DEFAULT_LAYER);
        optionsFirmware.setLayer(OptionMicroPython, JLayeredPane.DEFAULT_LAYER);

        optionsOther.setBorder(BorderFactory.createTitledBorder(null, "Other", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N

        FileAutoSaveDisk.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        FileAutoSaveDisk.setSelected(true);
        FileAutoSaveDisk.setText("AutoSave file to disk before save to ESP");
        FileAutoSaveDisk.setToolTipText("");
        FileAutoSaveDisk.setHorizontalAlignment(SwingConstants.LEFT);
        FileAutoSaveDisk.setHorizontalTextPosition(SwingConstants.RIGHT);
        FileAutoSaveDisk.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                FileAutoSaveDiskItemStateChanged();
            }
        });

        FileAutoSaveESP.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        FileAutoSaveESP.setSelected(true);
        FileAutoSaveESP.setText("AutoSave file to ESP after save to disk");
        FileAutoSaveESP.setToolTipText("");
        FileAutoSaveESP.setHorizontalAlignment(SwingConstants.LEFT);
        FileAutoSaveESP.setHorizontalTextPosition(SwingConstants.RIGHT);
        FileAutoSaveESP.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                FileAutoSaveESPItemStateChanged();
            }
        });

        FileAutoRun.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        FileAutoRun.setText("AutoRun file after save to ESP");
        FileAutoRun.setToolTipText("");
        FileAutoRun.setEnabled(false);
        FileAutoRun.setHorizontalAlignment(SwingConstants.LEFT);
        FileAutoRun.setHorizontalTextPosition(SwingConstants.RIGHT);
        FileAutoRun.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                FileAutoRunItemStateChanged();
            }
        });

        editorThemeLabel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        editorThemeLabel.setText("Editor color theme");

        EditorTheme.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        EditorTheme.setModel(new DefaultComboBoxModel(new String[]{"Default", "Dark", "Eclipse", "IDEA", "Visual Studio", "Default-alt"}));
        EditorTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditorThemeActionPerformed();
            }
        });

        UseExternalEditor.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        UseExternalEditor.setText("Use external editor");
        UseExternalEditor.setToolTipText("Use external editor. All files will be open in ReadOnly mode");
        UseExternalEditor.setHorizontalAlignment(SwingConstants.LEFT);
        UseExternalEditor.setHorizontalTextPosition(SwingConstants.RIGHT);
        UseExternalEditor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                UseExternalEditorItemStateChanged();
            }
        });

        GroupLayout OptionsOtherLayout = new GroupLayout(optionsOther);
        optionsOther.setLayout(OptionsOtherLayout);
        OptionsOtherLayout.setHorizontalGroup(
                OptionsOtherLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(FileAutoSaveDisk, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(FileAutoSaveESP, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(OptionsOtherLayout.createSequentialGroup()
                                .addComponent(editorThemeLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(EditorTheme, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addComponent(UseExternalEditor, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(FileAutoRun, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        OptionsOtherLayout.setVerticalGroup(
                OptionsOtherLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(OptionsOtherLayout.createSequentialGroup()
                                .addComponent(FileAutoSaveDisk, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(FileAutoSaveESP, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(UseExternalEditor)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(OptionsOtherLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(editorThemeLabel)
                                        .addComponent(EditorTheme, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FileAutoRun)
                                .addContainerGap(39, Short.MAX_VALUE))
        );
        optionsOther.setLayer(FileAutoSaveDisk, JLayeredPane.DEFAULT_LAYER);
        optionsOther.setLayer(FileAutoSaveESP, JLayeredPane.DEFAULT_LAYER);
        optionsOther.setLayer(FileAutoRun, JLayeredPane.DEFAULT_LAYER);
        optionsOther.setLayer(editorThemeLabel, JLayeredPane.DEFAULT_LAYER);
        optionsOther.setLayer(EditorTheme, JLayeredPane.DEFAULT_LAYER);
        optionsOther.setLayer(UseExternalEditor, JLayeredPane.DEFAULT_LAYER);

        optionsFileSendMode.setBorder(BorderFactory.createTitledBorder(null, "Send", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N
        optionsFileSendMode.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        DelayLabel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        DelayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        DelayLabel.setText("Delay after answer = 0 ms");
        DelayLabel.setToolTipText("It's not \"line delay\", as you known. It's delay between answer from ESP and send new data");

        Delay.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        Delay.setMajorTickSpacing(500);
        Delay.setMaximum(1000);
        Delay.setMinorTickSpacing(100);
        Delay.setPaintLabels(true);
        Delay.setPaintTicks(true);
        Delay.setSnapToTicks(true);
        Delay.setToolTipText("Delay between answer from ESP and send new data");
        Delay.setValue(0);
        Delay.setAlignmentY(1.0F);
        Delay.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                DelayStateChanged();
            }
        });

        AnswerDelayLabel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        AnswerDelayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        AnswerDelayLabel.setText("Answer timeout = 3 s");
        AnswerDelayLabel.setToolTipText("How many time we waiting answer from ESP8266");

        AnswerDelay.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        AnswerDelay.setMajorTickSpacing(5);
        AnswerDelay.setMaximum(10);
        AnswerDelay.setMinorTickSpacing(1);
        AnswerDelay.setPaintLabels(true);
        AnswerDelay.setPaintTicks(true);
        AnswerDelay.setSnapToTicks(true);
        AnswerDelay.setToolTipText("Maximum time for waiting firmware answer");
        AnswerDelay.setValue(3);
        AnswerDelay.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                AnswerDelayStateChanged();
            }
        });

        DumbMode.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        DumbMode.setText("\"Dumb Mode\", never check answers");
        DumbMode.setToolTipText("");
        DumbMode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DumbModeItemStateChanged();
            }
        });

        LineDelayLabel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        LineDelayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        LineDelayLabel.setText("Line delay for \"Dumb Mode\" = 200 ms");
        LineDelayLabel.setToolTipText("It's usual \"line delay\", as you known.");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, DumbMode, org.jdesktop.beansbinding.ELProperty.create("${selected}"), LineDelayLabel, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        LineDelay.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        LineDelay.setMajorTickSpacing(500);
        LineDelay.setMaximum(1000);
        LineDelay.setMinorTickSpacing(100);
        LineDelay.setPaintLabels(true);
        LineDelay.setPaintTicks(true);
        LineDelay.setSnapToTicks(true);
        LineDelay.setToolTipText("Fixed delay between lines");
        LineDelay.setValue(200);
        LineDelay.setAlignmentY(1.0F);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, DumbMode, org.jdesktop.beansbinding.ELProperty.create("${selected}"), LineDelay, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        LineDelay.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                LineDelayStateChanged();
            }
        });

        TurboMode.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        TurboMode.setText("\"Turbo Mode\"");
        TurboMode.setToolTipText("");
        TurboMode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
            }
        });
        TurboMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TurboModeActionPerformed();
            }
        });

        GroupLayout OptionsFileSendModeLayout = new GroupLayout(optionsFileSendMode);
        optionsFileSendMode.setLayout(OptionsFileSendModeLayout);
        OptionsFileSendModeLayout.setHorizontalGroup(
                OptionsFileSendModeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(OptionsFileSendModeLayout.createSequentialGroup()
                                .addGroup(OptionsFileSendModeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(OptionsFileSendModeLayout.createSequentialGroup()
                                                .addGroup(OptionsFileSendModeLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(TurboMode, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(DelayLabel, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, OptionsFileSendModeLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(OptionsFileSendModeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(DumbMode, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(Delay, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(AnswerDelayLabel, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(AnswerDelay, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(LineDelayLabel, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(LineDelay, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        OptionsFileSendModeLayout.setVerticalGroup(
                OptionsFileSendModeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(OptionsFileSendModeLayout.createSequentialGroup()
                                .addComponent(TurboMode)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DelayLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Delay, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AnswerDelayLabel)
                                .addGap(1, 1, 1)
                                .addComponent(AnswerDelay, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DumbMode)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LineDelayLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LineDelay, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        optionsFileSendMode.setLayer(DelayLabel, JLayeredPane.DEFAULT_LAYER);
        optionsFileSendMode.setLayer(Delay, JLayeredPane.DEFAULT_LAYER);
        optionsFileSendMode.setLayer(AnswerDelayLabel, JLayeredPane.DEFAULT_LAYER);
        optionsFileSendMode.setLayer(AnswerDelay, JLayeredPane.DEFAULT_LAYER);
        optionsFileSendMode.setLayer(DumbMode, JLayeredPane.DEFAULT_LAYER);
        optionsFileSendMode.setLayer(LineDelayLabel, JLayeredPane.DEFAULT_LAYER);
        optionsFileSendMode.setLayer(LineDelay, JLayeredPane.DEFAULT_LAYER);
        optionsFileSendMode.setLayer(TurboMode, JLayeredPane.DEFAULT_LAYER);

        jLayeredPane2.setBorder(BorderFactory.createTitledBorder(null, "Data scrollback", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel1.setText("Terminal");
        jLabel1.setPreferredSize(new java.awt.Dimension(40, 20));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel7.setText("Log");
        jLabel7.setPreferredSize(new java.awt.Dimension(17, 23));

        TerminalMaxSize.setHorizontalAlignment(JTextField.RIGHT);
        TerminalMaxSize.setText("100");
        TerminalMaxSize.setToolTipText("");
        TerminalMaxSize.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TerminalMaxSizeFocusLost();
            }
        });

        LogMaxSize.setHorizontalAlignment(JTextField.RIGHT);
        LogMaxSize.setText("10");
        LogMaxSize.setToolTipText("");
        LogMaxSize.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                LogMaxSizeFocusLost();
            }
        });
        LogMaxSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        jLabel8.setText("kb");

        jLabel9.setText("kb");

        GroupLayout jLayeredPane2Layout = new GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
                jLayeredPane2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jLayeredPane2Layout.createSequentialGroup()
                                .addGroup(jLayeredPane2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel7, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jLayeredPane2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(TerminalMaxSize, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                                        .addComponent(LogMaxSize))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jLayeredPane2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel9))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane2Layout.setVerticalGroup(
                jLayeredPane2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jLayeredPane2Layout.createSequentialGroup()
                                .addGroup(jLayeredPane2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(TerminalMaxSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jLayeredPane2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(LogMaxSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9)))
        );
        jLayeredPane2.setLayer(jLabel1, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jLabel7, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(TerminalMaxSize, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(LogMaxSize, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jLabel8, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jLabel9, JLayeredPane.DEFAULT_LAYER);

        jLayeredPane3.setBorder(BorderFactory.createTitledBorder(null, "SerialPort", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N

        CustomPortName.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        CustomPortName.setHorizontalAlignment(JTextField.LEFT);
        CustomPortName.setText("/dev/AnySerialDevice");
        CustomPortName.setToolTipText("");
        CustomPortName.setEnabled(false);
        CustomPortName.setMinimumSize(new java.awt.Dimension(50, 19));
        CustomPortName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                CustomPortNameFocusLost();
            }
        });

        UseCustomPortName.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        UseCustomPortName.setText("Use custom serial port name");
        UseCustomPortName.setToolTipText("Use custom serial port name (AutoScan will be disabled)");
        UseCustomPortName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UseCustomPortNameActionPerformed();
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel10.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel10.setText("(AutoScan will be disabled)");
        jLabel10.setPreferredSize(new java.awt.Dimension(17, 23));

        AutodetectFirmware.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        AutodetectFirmware.setText("Autodetect firmware");
        AutodetectFirmware.setToolTipText("Use custom serial port name (AutoScan will be disabled)");
        AutodetectFirmware.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                AutodetectFirmwareItemStateChanged();
            }
        });

        GroupLayout jLayeredPane3Layout = new GroupLayout(jLayeredPane3);
        jLayeredPane3.setLayout(jLayeredPane3Layout);
        jLayeredPane3Layout.setHorizontalGroup(
                jLayeredPane3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(UseCustomPortName, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                        .addComponent(jLabel10, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CustomPortName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AutodetectFirmware, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jLayeredPane3Layout.setVerticalGroup(
                jLayeredPane3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jLayeredPane3Layout.createSequentialGroup()
                                .addComponent(UseCustomPortName)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(CustomPortName, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(AutodetectFirmware))
        );
        jLayeredPane3.setLayer(CustomPortName, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane3.setLayer(UseCustomPortName, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane3.setLayer(jLabel10, JLayeredPane.DEFAULT_LAYER);
        jLayeredPane3.setLayer(AutodetectFirmware, JLayeredPane.DEFAULT_LAYER);

        GroupLayout NodeMCUSettingsLayout = new GroupLayout(nodeMCUSettings);
        nodeMCUSettings.setLayout(NodeMCUSettingsLayout);
        NodeMCUSettingsLayout.setHorizontalGroup(
                NodeMCUSettingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(NodeMCUSettingsLayout.createSequentialGroup()
                                .addGroup(NodeMCUSettingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(optionsOther)
                                        .addComponent(optionsFirmware)
                                        .addComponent(optionsFileSendMode, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(NodeMCUSettingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLayeredPane2)
                                        .addComponent(jLayeredPane3))
                                .addContainerGap(135, Short.MAX_VALUE))
        );
        NodeMCUSettingsLayout.setVerticalGroup(
                NodeMCUSettingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(NodeMCUSettingsLayout.createSequentialGroup()
                                .addGroup(NodeMCUSettingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(NodeMCUSettingsLayout.createSequentialGroup()
                                                .addComponent(optionsFirmware, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(optionsOther, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(NodeMCUSettingsLayout.createSequentialGroup()
                                                .addComponent(jLayeredPane3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLayeredPane2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(optionsFileSendMode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 173, Short.MAX_VALUE))
        );
        nodeMCUSettings.setLayer(optionsFirmware, JLayeredPane.DEFAULT_LAYER);
        nodeMCUSettings.setLayer(optionsOther, JLayeredPane.DEFAULT_LAYER);
        nodeMCUSettings.setLayer(optionsFileSendMode, JLayeredPane.DEFAULT_LAYER);
        nodeMCUSettings.setLayer(jLayeredPane2, JLayeredPane.DEFAULT_LAYER);
        nodeMCUSettings.setLayer(jLayeredPane3, JLayeredPane.DEFAULT_LAYER);

        TextTab.addTab("Settings", new ImageIcon(getClass().getResource("/resources/settings2.png")), nodeMCUSettings, "Settings for file sending"); // NOI18N

        GroupLayout NodeMCULayout = new GroupLayout(nodeMCU);
        nodeMCU.setLayout(NodeMCULayout);
        NodeMCULayout.setHorizontalGroup(
                NodeMCULayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(TextTab, GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
        );
        NodeMCULayout.setVerticalGroup(
                NodeMCULayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(TextTab, GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
        );

        TextTab.getAccessibleContext().setAccessibleName("NewFile");

        LeftTab.addTab("NodeMCU+MicroPython", nodeMCU);

        topWiFiStaFiller.setOpaque(true);


        cmdGetCWJAP.setFont(cmdGetCWJAP.getFont().deriveFont((float) 12));
        cmdGetCWJAP.setIcon(new ImageIcon(getClass().getResource("/resources/information.png"))); // NOI18N
        cmdGetCWJAP.setText("CWJAP? - Connection info");
        cmdGetCWJAP.setToolTipText("Query AP’s info which is connect by ESP8266");
        cmdGetCWJAP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdGetCWJAP.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdGetCWJAP.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdGetCWJAP.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdGetCWJAP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        cmdSetCWJAP.setFont(cmdSetCWJAP.getFont().deriveFont((float) 12));
        cmdSetCWJAP.setIcon(new ImageIcon(getClass().getResource("/resources/connect3.png"))); // NOI18N
        cmdSetCWJAP.setText("CWJAP Connect to AP");
        cmdSetCWJAP.setToolTipText("Connect to WiFi Access Point");
        cmdSetCWJAP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdSetCWJAP.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdSetCWJAP.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdSetCWJAP.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdSetCWJAP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        SSID.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SSID.setText("SSID");
        SSID.setToolTipText("Enter WiFi SSID");
        SSID.setMaximumSize(new java.awt.Dimension(100, 23));
        SSID.setMinimumSize(new java.awt.Dimension(100, 23));
        SSID.setPreferredSize(new java.awt.Dimension(100, 23));
        SSID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SSIDFocusGained();
            }
        });
        SSID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        PASS.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        PASS.setText("password");
        PASS.setToolTipText("Enter WiFi password");
        PASS.setMaximumSize(new java.awt.Dimension(104, 23));
        PASS.setMinimumSize(new java.awt.Dimension(104, 23));
        PASS.setPreferredSize(new java.awt.Dimension(104, 23));
        PASS.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PASSFocusGained();
            }
        });
        PASS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        cmdSetCWQAP.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdSetCWQAP.setIcon(new ImageIcon(getClass().getResource("/resources/disconnect3.png"))); // NOI18N
        cmdSetCWQAP.setText("CWQAP - Disconnect from AP");
        cmdSetCWQAP.setToolTipText("Disconnect from WiFi Access Point");
        cmdSetCWQAP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdSetCWQAP.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdSetCWQAP.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdSetCWQAP.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdSetCWQAP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        cmdGetCIPSTAMAC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdGetCIPSTAMAC.setIcon(new ImageIcon(getClass().getResource("/resources/get.png"))); // NOI18N
        cmdGetCIPSTAMAC.setText("CIPSTAMAC? Get MAC");
        cmdGetCIPSTAMAC.setToolTipText("Get Station MAC address");
        cmdGetCIPSTAMAC.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdGetCIPSTAMAC.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdGetCIPSTAMAC.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdGetCIPSTAMAC.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdGetCIPSTAMAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        cmdSetCIPSTAMAC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdSetCIPSTAMAC.setIcon(new ImageIcon(getClass().getResource("/resources/set.png"))); // NOI18N
        cmdSetCIPSTAMAC.setText("CIPSTAMAC= Set MAC Station");
        cmdSetCIPSTAMAC.setToolTipText("Set Station MAC address");
        cmdSetCIPSTAMAC.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdSetCIPSTAMAC.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdSetCIPSTAMAC.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdSetCIPSTAMAC.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdSetCIPSTAMAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        try {
            MAC.setFormatterFactory(new DefaultFormatterFactory(new javax.swing.text.MaskFormatter("AA:AA:AA:AA:AA:AA")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        MAC.setText("FF:FF:FF:FF:FF:FF");
        MAC.setToolTipText("Station MAC address");
        MAC.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        MAC.setMaximumSize(new java.awt.Dimension(210, 23));
        MAC.setMinimumSize(new java.awt.Dimension(210, 23));
        MAC.setPreferredSize(new java.awt.Dimension(210, 23));

        cmdGetCIPSTA.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdGetCIPSTA.setIcon(new ImageIcon(getClass().getResource("/resources/get.png"))); // NOI18N
        cmdGetCIPSTA.setText("CIPSTA? Get Station IP");
        cmdGetCIPSTA.setToolTipText("Get Station IP address");
        cmdGetCIPSTA.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdGetCIPSTA.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdGetCIPSTA.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdGetCIPSTA.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdGetCIPSTA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        cmdSetCIPSTA.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdSetCIPSTA.setIcon(new ImageIcon(getClass().getResource("/resources/set.png"))); // NOI18N
        cmdSetCIPSTA.setText("CIPSTA= Set Station IP");
        cmdSetCIPSTA.setToolTipText("Set Station IP address");
        cmdSetCIPSTA.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdSetCIPSTA.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdSetCIPSTA.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdSetCIPSTA.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdSetCIPSTA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        stationIP.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        stationIP.setText("192.168.1.50");
        stationIP.setMaximumSize(new java.awt.Dimension(210, 23));
        stationIP.setMinimumSize(new java.awt.Dimension(210, 23));
        stationIP.setPreferredSize(new java.awt.Dimension(210, 23));


        cmdSetCWSAP.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdSetCWSAP.setIcon(new ImageIcon(getClass().getResource("/resources/server1_start.png"))); // NOI18N
        cmdSetCWSAP.setText("CWSAP=  Set softAP config");
        cmdSetCWSAP.setToolTipText("Set configuraton of softAP mode");
        cmdSetCWSAP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdSetCWSAP.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdSetCWSAP.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdSetCWSAP.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdSetCWSAP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        cmdGetCWSAP.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdGetCWSAP.setIcon(new ImageIcon(getClass().getResource("/resources/information.png"))); // NOI18N
        cmdGetCWSAP.setText("AT+CWSAP? Get softAP config");
        cmdGetCWSAP.setToolTipText("Get configuraton of softAP mode");
        cmdGetCWSAP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdGetCWSAP.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdGetCWSAP.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdGetCWSAP.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdGetCWSAP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        cmdGetCIPAPMAC.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdGetCIPAPMAC.setIcon(new ImageIcon(getClass().getResource("/resources/get.png"))); // NOI18N
        cmdGetCIPAPMAC.setText("CIPAPMAC Get softAP MAC");
        cmdGetCIPAPMAC.setToolTipText("Get MAC address of ESP8266 softAP");
        cmdGetCIPAPMAC.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdGetCIPAPMAC.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdGetCIPAPMAC.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdGetCIPAPMAC.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdGetCIPAPMAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        cmdGetCWLIF.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmdGetCWLIF.setIcon(new ImageIcon(getClass().getResource("/resources/get.png"))); // NOI18N
        cmdGetCWLIF.setText("CWLIF  Get connected clients IP");
        cmdGetCWLIF.setToolTipText("Get IP adresses softAP clients");
        cmdGetCWLIF.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdGetCWLIF.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdGetCWLIF.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdGetCWLIF.setPreferredSize(new java.awt.Dimension(210, 23));
        cmdGetCWLIF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });


        common.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Common parameters for commands", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        common.setMaximumSize(new java.awt.Dimension(445, 54));
        common.setMinimumSize(new java.awt.Dimension(445, 54));
        common.setPreferredSize(new java.awt.Dimension(445, 54));

        conn_id.setModel(new DefaultComboBoxModel(new String[]{"0", "1", "2", "3", "4"}));
        conn_id.setToolTipText("Connection ID");

        MUXGroup.add(single);
        single.setText("CIPMUX=0 - Single");
        single.setToolTipText("Single connection");
        single.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
            }
        });

        MUXGroup.add(multi);
        multi.setSelected(true);
        multi.setText("CIPMUX=1 - Multiple");
        multi.setToolTipText("Multiple connection");
        multi.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
            }
        });
        multi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel4.setText("Connection ID");

        GroupLayout commonLayout = new GroupLayout(common);
        common.setLayout(commonLayout);
        commonLayout.setHorizontalGroup(
                commonLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(commonLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(conn_id, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(multi)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(single, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE))
        );
        commonLayout.setVerticalGroup(
                commonLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(commonLayout.createSequentialGroup()
                                .addGroup(commonLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(conn_id, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(multi)
                                        .addComponent(single)
                                        .addComponent(jLabel4))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cmdGetCIPSTART.setIcon(new ImageIcon(getClass().getResource("/resources/information.png"))); // NOI18N
        cmdGetCIPSTART.setText("CIPSTART=? - Connection info");
        cmdGetCIPSTART.setToolTipText("Get the information of param");
        cmdGetCIPSTART.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        UDP.setBorder(BorderFactory.createTitledBorder("UDP only"));
        UDP.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
            }
        });

        udp_local_port.setToolTipText("Local UDP port");
        udp_local_port.setEnabled(false);

        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("Local port");

        udp_mode.setToolTipText("Local UDP port");
        udp_mode.setEnabled(false);

        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText("mode");

        GroupLayout UDPLayout = new GroupLayout(UDP);
        UDP.setLayout(UDPLayout);
        UDPLayout.setHorizontalGroup(
                UDPLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(UDPLayout.createSequentialGroup()
                                .addGroup(UDPLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(udp_local_port))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(UDPLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(udp_mode)
                                        .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(6, 6, 6))
        );
        UDPLayout.setVerticalGroup(
                UDPLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(UDPLayout.createSequentialGroup()
                                .addGroup(UDPLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(UDPLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(udp_local_port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(udp_mode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        UDP.setLayer(udp_local_port, JLayeredPane.DEFAULT_LAYER);
        UDP.setLayer(jLabel2, JLayeredPane.DEFAULT_LAYER);
        UDP.setLayer(udp_mode, JLayeredPane.DEFAULT_LAYER);
        UDP.setLayer(jLabel3, JLayeredPane.DEFAULT_LAYER);

        remote_address.setText("192.168.1.1");
        remote_address.setToolTipText("Remote IP address");

        remote_port.setText("80");
        remote_port.setToolTipText("Remote port");

        protocol.setModel(new DefaultComboBoxModel(new String[]{"TCP", "UDP"}));
        protocol.setToolTipText("Connection type: TCP or UDP");
        protocol.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
            }
        });

        cmdSetCIPSTART.setIcon(new ImageIcon(getClass().getResource("/resources/connect3.png"))); // NOI18N
        cmdSetCIPSTART.setText("AT+CIPSTART= - Start connection");
        cmdSetCIPSTART.setToolTipText("Start a connection as client");
        cmdSetCIPSTART.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSetCIPSTARTActionPerformed();
            }
        });

        data.setColumns(15);
        data.setRows(5);
        data.setTabSize(4);
        data.setText("GET / HTTP/1.0\nHost: 192.168.1.1\nConnection: keep-alive\nAccept: */*\n\n");
        data.setToolTipText("Data to send to remote client");
        data.setCaretPosition(1);
        jScrollData.setViewportView(data);

        cmdCIPSEND.setIcon(new ImageIcon(getClass().getResource("/resources/level up.png"))); // NOI18N
        cmdCIPSEND.setText("AT+CIPSEND= - Send data");
        cmdCIPSEND.setToolTipText("Send data to remote client");
        cmdCIPSEND.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCIPSENDActionPerformed();
            }
        });

        cmdCIPSENDinteractive.setText("AT+CIPSEND - Interactive ");
        cmdCIPSENDinteractive.setToolTipText("Send data to remote client in interactive mode. After all data sent, type \"+++\"");
        cmdCIPSENDinteractive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        cmdSetCIPCLOSE.setIcon(new ImageIcon(getClass().getResource("/resources/disconnect3.png"))); // NOI18N
        cmdSetCIPCLOSE.setText("AT+CIPCLOSE - Close connection");
        cmdSetCIPCLOSE.setToolTipText("Close current connection in Single mode and close connection <ID> in Multiple mode");
        cmdSetCIPCLOSE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSetCIPCLOSEActionPerformed();
            }
        });

        GroupLayout TCPclientBottomPaneLayout = new GroupLayout(TCPclientBottomPane);
        TCPclientBottomPane.setLayout(TCPclientBottomPaneLayout);
        TCPclientBottomPaneLayout.setHorizontalGroup(
                TCPclientBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(TCPclientBottomPaneLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(TCPclientBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(TCPclientBottomPaneLayout.createSequentialGroup()
                                                .addComponent(UDP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(TCPclientBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(TCPclientBottomPaneLayout.createSequentialGroup()
                                                                .addComponent(remote_address, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(remote_port, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(protocol, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(cmdSetCIPSTART, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(TCPclientBottomPaneLayout.createSequentialGroup()
                                                .addComponent(jScrollData, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(TCPclientBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(cmdCIPSENDinteractive, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cmdCIPSEND, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cmdSetCIPCLOSE, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                        .addComponent(cmdGetCIPSTART, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE))))
                                .addGap(33, 33, 33))
                        .addGroup(TCPclientBottomPaneLayout.createSequentialGroup()
                                .addComponent(common, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        TCPclientBottomPaneLayout.setVerticalGroup(
                TCPclientBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(TCPclientBottomPaneLayout.createSequentialGroup()
                                .addComponent(common, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGroup(TCPclientBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(TCPclientBottomPaneLayout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(TCPclientBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(remote_address, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(remote_port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(protocol, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(cmdSetCIPSTART))
                                        .addGroup(TCPclientBottomPaneLayout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(UDP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addGap(413, 413, 413)
                                .addGroup(TCPclientBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(TCPclientBottomPaneLayout.createSequentialGroup()
                                                .addComponent(cmdGetCIPSTART)
                                                .addGap(7, 7, 7)
                                                .addComponent(cmdCIPSEND)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cmdCIPSENDinteractive)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmdSetCIPCLOSE))
                                        .addComponent(jScrollData))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel6.setText("Server timeout");

        GroupLayout TCPServerBottomPaneLayout = new GroupLayout(TCPServerBottomPane);
        TCPServerBottomPane.setLayout(TCPServerBottomPaneLayout);
        TCPServerBottomPaneLayout.setHorizontalGroup(
                TCPServerBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(TCPServerBottomPaneLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(TCPServerBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(cmdSetCIPSTO, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(TCPServerBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(TCPServerBottomPaneLayout.createSequentialGroup()
                                                        .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(serverTimeout, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(TCPServerBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(cmdSetCIPMODE1, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(cmdGetCIPSTO, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(cmdGetCIPMODE, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdSetCIPMODE0, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(TCPServerBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(TCPServerBottomPaneLayout.createSequentialGroup()
                                                .addComponent(ServerMode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel5)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ServerPort, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(cmdSetCIPSERVER, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)))
        );
        TCPServerBottomPaneLayout.setVerticalGroup(
                TCPServerBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(TCPServerBottomPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(TCPServerBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(ServerPort, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ServerMode, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdGetCIPMODE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(TCPServerBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(cmdSetCIPSERVER, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdSetCIPMODE0, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmdSetCIPMODE1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmdGetCIPSTO, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(TCPServerBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(serverTimeout, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmdSetCIPSTO, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        TCPServerBottomPane.setLayer(cmdGetCIPMODE, JLayeredPane.DEFAULT_LAYER);
        TCPServerBottomPane.setLayer(cmdSetCIPMODE0, JLayeredPane.DEFAULT_LAYER);
        TCPServerBottomPane.setLayer(cmdSetCIPMODE1, JLayeredPane.DEFAULT_LAYER);
        TCPServerBottomPane.setLayer(cmdSetCIPSERVER, JLayeredPane.DEFAULT_LAYER);
        TCPServerBottomPane.setLayer(ServerMode, JLayeredPane.DEFAULT_LAYER);
        TCPServerBottomPane.setLayer(jLabel5, JLayeredPane.DEFAULT_LAYER);
        TCPServerBottomPane.setLayer(ServerPort, JLayeredPane.DEFAULT_LAYER);
        TCPServerBottomPane.setLayer(cmdGetCIPSTO, JLayeredPane.DEFAULT_LAYER);
        TCPServerBottomPane.setLayer(cmdSetCIPSTO, JLayeredPane.DEFAULT_LAYER);
        TCPServerBottomPane.setLayer(serverTimeout, JLayeredPane.DEFAULT_LAYER);
        TCPServerBottomPane.setLayer(jLabel6, JLayeredPane.DEFAULT_LAYER);

        GroupLayout AT_ServerLayout = new GroupLayout(AT_Server);
        AT_Server.setLayout(AT_ServerLayout);
        AT_ServerLayout.setHorizontalGroup(
                AT_ServerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, AT_ServerLayout.createSequentialGroup()
                                .addGroup(AT_ServerLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(TCPServerBottomPane, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
                                        .addComponent(TCPServerTopFiller, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE))
                                .addContainerGap())
        );
        AT_ServerLayout.setVerticalGroup(
                AT_ServerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, AT_ServerLayout.createSequentialGroup()
                                .addComponent(TCPServerTopFiller, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TCPServerBottomPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TCP_common.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Common TCP commands"));
        TCP_common.setAlignmentX(0.0F);
        TCP_common.setAlignmentY(0.0F);
        TCP_common.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TCP_common.setMaximumSize(new java.awt.Dimension(445, 45));
        TCP_common.setMinimumSize(new java.awt.Dimension(445, 45));
        TCP_common.setOpaque(true);
        TCP_common.setLayout(new java.awt.FlowLayout());

        WiFi_common.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Common WiFi commands"));
        WiFi_common.setAlignmentX(0.0F);
        WiFi_common.setAlignmentY(0.0F);
        WiFi_common.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        WiFi_common.setName(""); // NOI18N
        WiFi_common.setOpaque(true);
        WiFi_common.setPreferredSize(new java.awt.Dimension(445, 110));
        WiFi_common.setLayout(new java.awt.FlowLayout());

        cmdGetHelpCWMODE.setText("CWMODE=? - Get available modes");
        cmdGetHelpCWMODE.setToolTipText("Get value scope of WiFi mode (CommandWifiMODE)");
        cmdGetHelpCWMODE.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdGetHelpCWMODE.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdGetHelpCWMODE.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdGetHelpCWMODE.setPreferredSize(new java.awt.Dimension(210, 23));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cmdGetHelpCWMODE, org.jdesktop.beansbinding.ELProperty.create("${font}"), cmdGetHelpCWMODE, org.jdesktop.beansbinding.BeanProperty.create("font"));
        bindingGroup.addBinding(binding);

        cmdGetHelpCWMODE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        WiFi_common.add(cmdGetHelpCWMODE);

        cmdSetCWMODE1.setText("CWMODE=1 Station");
        cmdSetCWMODE1.setToolTipText("Set ESP8266 WiFi mode 1 - Station");
        cmdSetCWMODE1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdSetCWMODE1.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdSetCWMODE1.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdSetCWMODE1.setPreferredSize(new java.awt.Dimension(210, 23));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cmdGetHelpCWMODE, org.jdesktop.beansbinding.ELProperty.create("${font}"), cmdSetCWMODE1, org.jdesktop.beansbinding.BeanProperty.create("font"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cmdGetHelpCWMODE, org.jdesktop.beansbinding.ELProperty.create("${font}"), cmdSetCWMODE2, org.jdesktop.beansbinding.BeanProperty.create("font"));
        bindingGroup.addBinding(binding);

        cmdSetCWLAP.setIcon(new ImageIcon(getClass().getResource("/resources/search again.png"))); // NOI18N
        cmdSetCWLAP.setText("CWLAP - Get AP list");
        cmdSetCWLAP.setToolTipText("Lists all available WiFi Access Points");
        cmdSetCWLAP.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdSetCWLAP.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdSetCWLAP.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdSetCWLAP.setPreferredSize(new java.awt.Dimension(210, 23));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cmdGetHelpCWMODE, org.jdesktop.beansbinding.ELProperty.create("${font}"), cmdSetCWLAP, org.jdesktop.beansbinding.BeanProperty.create("font"));
        bindingGroup.addBinding(binding);

        cmdSetCWMODE3.setText("CWMODE=3 softAP + Station");
        cmdSetCWMODE3.setToolTipText("Set ESP8266 WiFi mode 3 - Soft Access Point mode + Station");
        cmdSetCWMODE3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdSetCWMODE3.setMaximumSize(new java.awt.Dimension(210, 23));
        cmdSetCWMODE3.setMinimumSize(new java.awt.Dimension(210, 23));
        cmdSetCWMODE3.setPreferredSize(new java.awt.Dimension(210, 23));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cmdGetHelpCWMODE, org.jdesktop.beansbinding.ELProperty.create("${font}"), cmdSetCWMODE3, org.jdesktop.beansbinding.BeanProperty.create("font"));
        bindingGroup.addBinding(binding);

        DHCP.setModel(new DefaultComboBoxModel(new String[]{"0 - Enable DHCP", "1 - Disable DHCP"}));
        DHCP.setMaximumSize(new java.awt.Dimension(114, 23));
        DHCP.setMinimumSize(new java.awt.Dimension(114, 23));
        DHCP.setPreferredSize(new java.awt.Dimension(114, 23));
        WiFi_common.add(DHCP);

        DHCPmode.setModel(new DefaultComboBoxModel(new String[]{"0 - Set softAP", "1 - Set Station", "2 - Set both AP&Sta"}));
        DHCPmode.setSelectedIndex(1);
        DHCPmode.setMaximumSize(new java.awt.Dimension(90, 23));
        DHCPmode.setMinimumSize(new java.awt.Dimension(90, 23));
        DHCPmode.setPreferredSize(new java.awt.Dimension(90, 23));
        WiFi_common.add(DHCPmode);

        comingSoon1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comingSoon1.setHorizontalAlignment(SwingConstants.CENTER);
        comingSoon1.setText("Coming soon...");

        GroupLayout LeftBasePaneLayout = new GroupLayout(LeftBasePane);
        LeftBasePane.setLayout(LeftBasePaneLayout);
        LeftBasePaneLayout.setHorizontalGroup(
                LeftBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(LeftTab, GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
        );
        LeftBasePaneLayout.setVerticalGroup(
                LeftBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(LeftTab, GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE)
        );
        LeftBasePane.setLayer(LeftTab, JLayeredPane.DEFAULT_LAYER);

        LeftTab.getAccessibleContext().setAccessibleName("LeftTab");

        horizontSplit.setLeftComponent(LeftBasePane);

        LEDPanel.setMaximumSize(new java.awt.Dimension(392, 25));
        LEDPanel.setMinimumSize(new java.awt.Dimension(392, 25));

        PortOpenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        PortOpenLabel.setIcon(new ImageIcon(getClass().getResource("/resources/led_grey.png"))); // NOI18N
        PortOpenLabel.setText("Open");
        PortOpenLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        PortOpenLabel.setMaximumSize(new java.awt.Dimension(50, 25));
        PortOpenLabel.setMinimumSize(new java.awt.Dimension(50, 25));
        PortOpenLabel.setPreferredSize(new java.awt.Dimension(50, 25));
        PortOpenLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        PortCTS.setHorizontalAlignment(SwingConstants.CENTER);
        PortCTS.setIcon(new ImageIcon(getClass().getResource("/resources/led_grey.png"))); // NOI18N
        PortCTS.setText("CTS");
        PortCTS.setHorizontalTextPosition(SwingConstants.CENTER);
        PortCTS.setMaximumSize(new java.awt.Dimension(50, 25));
        PortCTS.setMinimumSize(new java.awt.Dimension(50, 25));
        PortCTS.setPreferredSize(new java.awt.Dimension(50, 25));
        PortCTS.setVerticalTextPosition(SwingConstants.BOTTOM);

        PortDTR.setIcon(new ImageIcon(getClass().getResource("/resources/led_grey.png"))); // NOI18N
        PortDTR.setText("DTR");
        PortDTR.setToolTipText("");
        PortDTR.setHorizontalTextPosition(SwingConstants.CENTER);
        PortDTR.setVerticalAlignment(SwingConstants.TOP);
        PortDTR.setVerticalTextPosition(SwingConstants.BOTTOM);
        PortDTR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PortDTRActionPerformed();
            }
        });

        PortRTS.setIcon(new ImageIcon(getClass().getResource("/resources/led_grey.png"))); // NOI18N
        PortRTS.setText("RTS");
        PortRTS.setToolTipText("");
        PortRTS.setHorizontalTextPosition(SwingConstants.CENTER);
        PortRTS.setVerticalAlignment(SwingConstants.TOP);
        PortRTS.setVerticalTextPosition(SwingConstants.BOTTOM);
        PortRTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PortRTSActionPerformed();
            }
        });

        Open.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        Open.setIcon(new ImageIcon(getClass().getResource("/resources/connect1.png"))); // NOI18N
        Open.setText("Open");
        Open.setToolTipText("Open/Close selected serial port");
        Open.setIconTextGap(2);
        Open.setMargin(new java.awt.Insets(1, 1, 1, 1));
        Open.setMaximumSize(new java.awt.Dimension(100, 25));
        Open.setMinimumSize(new java.awt.Dimension(85, 25));
        Open.setPreferredSize(new java.awt.Dimension(80, 25));
        Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenActionPerformed();
            }
        });

        Speed.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Speed.setModel(new DefaultComboBoxModel(new String[]{"1200", "2400", "4800", "9600", "19200", "38400", "57600", "74880", "115200", "230400", "460800", "921600"}));
        Speed.setToolTipText("Select baud rate");
        Speed.setMaximumSize(new java.awt.Dimension(80, 25));
        Speed.setMinimumSize(new java.awt.Dimension(80, 25));
        Speed.setPreferredSize(new java.awt.Dimension(80, 25));
        Speed.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SpeedItemStateChanged();
            }
        });
        Speed.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
            }
        });
        Speed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SpeedActionPerformed();
            }
        });

        ReScan.setIcon(new ImageIcon(getClass().getResource("/resources/refresh3.png"))); // NOI18N
        ReScan.setToolTipText("Scan system for available serial port");
        ReScan.setMaximumSize(new java.awt.Dimension(25, 25));
        ReScan.setMinimumSize(new java.awt.Dimension(25, 25));
        ReScan.setPreferredSize(new java.awt.Dimension(25, 25));
        ReScan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        AutoScroll.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        AutoScroll.setSelected(true);
        AutoScroll.setText("AutoScroll");
        AutoScroll.setToolTipText("Terminal AutoScroll Enable/Disable");
        AutoScroll.setMinimumSize(new java.awt.Dimension(70, 25));
        AutoScroll.setPreferredSize(new java.awt.Dimension(60, 25));
        AutoScroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AutoScrollActionPerformed();
            }
        });

        Port.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        Port.setMaximumRowCount(20);
        Port.setModel(new DefaultComboBoxModel(new String[]{"COM1", "COM3"}));
        Port.setToolTipText("Serial port chooser");
        Port.setMaximumSize(new java.awt.Dimension(150, 25));
        Port.setMinimumSize(new java.awt.Dimension(150, 25));
        Port.setPreferredSize(new java.awt.Dimension(150, 25));
        Port.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
            }
        });
        Port.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        EOL.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        EOL.setText("EOL");
        EOL.setToolTipText("EOL visible Enable/Disable");
        EOL.setMinimumSize(new java.awt.Dimension(70, 25));
        EOL.setPreferredSize(new java.awt.Dimension(60, 25));
        EOL.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EOLItemStateChanged();
            }
        });

        GroupLayout LEDPanelLayout = new GroupLayout(LEDPanel);
        LEDPanel.setLayout(LEDPanelLayout);
        LEDPanelLayout.setHorizontalGroup(
                LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                .addComponent(PortDTR)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(PortRTS))
                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                .addComponent(PortOpenLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(PortCTS, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Open, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                .addComponent(ReScan, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(AutoScroll, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(EOL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(Speed, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(Port, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        LEDPanelLayout.setVerticalGroup(
                LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                .addComponent(Port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(GroupLayout.Alignment.LEADING, LEDPanelLayout.createSequentialGroup()
                                                .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(ReScan, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(LEDPanelLayout.createSequentialGroup()
                                                                .addComponent(AutoScroll, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(EOL, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Speed, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(Open, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(GroupLayout.Alignment.LEADING, LEDPanelLayout.createSequentialGroup()
                                                        .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(PortOpenLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(PortCTS, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(LEDPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addComponent(PortDTR)
                                                                .addComponent(PortRTS)))))
                                .addGap(0, 0, 0))
        );
        LEDPanel.setLayer(PortOpenLabel, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(PortCTS, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(PortDTR, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(PortRTS, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(Open, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(Speed, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(ReScan, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(AutoScroll, JLayeredPane.DEFAULT_LAYER);
        LEDPanel.setLayer(Port, JLayeredPane.DEFAULT_LAYER);

        Port.getAccessibleContext().setAccessibleName("");
        LEDPanel.setLayer(EOL, JLayeredPane.DEFAULT_LAYER);

        rightBottomPane.setAlignmentX(0.0F);
        rightBottomPane.setAlignmentY(0.0F);

        LF.setFont(LF.getFont().deriveFont(LF.getFont().getSize() - 4f));
        LF.setSelected(true);
        LF.setText("LF");
        LF.setToolTipText("Add LF at end of line");
        LF.setAlignmentY(0.0F);
        LF.setEnabled(false);
        LF.setIconTextGap(0);
        LF.setMargin(new java.awt.Insets(0, 0, 0, 0));

        SendCommand.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        SendCommand.setIcon(new ImageIcon(getClass().getResource("/resources/left.png"))); // NOI18N
        SendCommand.setToolTipText("");
        SendCommand.setAlignmentY(0.0F);
        SendCommand.setEnabled(false);
        SendCommand.setText("Send");
        SendCommand.setMargin(new java.awt.Insets(0, 0, 0, 0));
        SendCommand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        CR.setFont(CR.getFont().deriveFont(CR.getFont().getSize() - 4f));
        CR.setSelected(true);
        CR.setText("CR");
        CR.setToolTipText("Add CR at end of line");
        CR.setAlignmentY(0.0F);
        CR.setEnabled(false);
        CR.setIconTextGap(0);
        CR.setMargin(new java.awt.Insets(0, 0, 0, 0));
        CR.setName(""); // NOI18N

        Command.setEditable(true);
        Command.setMaximumRowCount(20);
        Command.setModel(new DefaultComboBoxModel(new String[]{"AT", "AT+GMR", "AT+RST", ""}));
        Command.setToolTipText("Command to send");
        Command.setAlignmentX(0.0F);
        Command.setAlignmentY(0.0F);
        Command.setAutoscrolls(true);
        Command.setEnabled(false);
        Command.setName("Command"); // NOI18N
        Command.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
            }
        });
        Command.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CommandActionPerformed(evt);
            }
        });
        Command.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
            }
        });

        GroupLayout RightBottomPaneLayout = new GroupLayout(rightBottomPane);
        rightBottomPane.setLayout(RightBottomPaneLayout);
        RightBottomPaneLayout.setHorizontalGroup(
                RightBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(RightBottomPaneLayout.createSequentialGroup()
                                .addComponent(Command, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SendCommand, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(RightBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(CR)
                                        .addComponent(LF))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        RightBottomPaneLayout.setVerticalGroup(
                RightBottomPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(SendCommand, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.LEADING, RightBottomPaneLayout.createSequentialGroup()
                                .addComponent(CR)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LF)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(Command, GroupLayout.Alignment.LEADING)
        );
        rightBottomPane.setLayer(LF, JLayeredPane.DEFAULT_LAYER);
        rightBottomPane.setLayer(SendCommand, JLayeredPane.DEFAULT_LAYER);

        SendCommand.getAccessibleContext().setAccessibleName("");
        rightBottomPane.setLayer(CR, JLayeredPane.DEFAULT_LAYER);
        rightBottomPane.setLayer(Command, JLayeredPane.DEFAULT_LAYER);
        Command.getAccessibleContext().setAccessibleName("Command");

        rightBigPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rightBigPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
            }
        });

        RightFilesSplitPane.setDividerLocation(300);
        RightFilesSplitPane.setAutoscrolls(true);
        RightFilesSplitPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                RightFilesSplitPanePropertyChange(evt);
            }
        });

        RightSplitPane.setBorder(null);
        RightSplitPane.setDividerLocation(320);
        RightSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        RightSplitPane.setToolTipText("");
        RightSplitPane.setName(""); // NOI18N
        RightSplitPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                RightSplitPanePropertyChange(evt);
            }
        });

        terminalPane.setToolTipText("Terminal window");
        terminalPane.setMaximumSize(new java.awt.Dimension(100, 100));
        terminalPane.setMinimumSize(new java.awt.Dimension(100, 100));
        terminalPane.setName(""); // NOI18N
        terminalPane.setPreferredSize(new java.awt.Dimension(100, 100));
        terminalPane.setViewportView(Terminal);

        Terminal.setEditable(false);
        Terminal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Terminal.setColumns(20);
        Terminal.setRows(50);
        Terminal.setTabSize(4);
        Terminal.setToolTipText("");
        Terminal.setWrapStyleWord(false);
        Terminal.setBracketMatchingEnabled(false);
        Terminal.setCloseCurlyBraces(false);
        Terminal.setCloseMarkupTags(false);
        Terminal.setDragEnabled(false);
        Terminal.setFadeCurrentLineHighlight(true);
        Terminal.setHighlightSecondaryLanguages(false);
        Terminal.setMaximumSize(new java.awt.Dimension(100, 100));
        Terminal.setMinimumSize(new java.awt.Dimension(100, 100));
        Terminal.setName(""); // NOI18N
        Terminal.setPopupMenu(contextMenuTerminal);
        Terminal.setSyntaxEditingStyle("text/LUA");
        Terminal.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
            }
        });
        terminalPane.setViewportView(Terminal);
        Terminal.getAccessibleContext().setAccessibleParent(terminalPane);

        RightSplitPane.setTopComponent(terminalPane);

        ScrollLog.setBorder(terminalPane.getBorder());
        ScrollLog.setToolTipText("Log");
        ScrollLog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ScrollLog.setAlignmentX(0.0F);
        ScrollLog.setAlignmentY(0.0F);

        Log.setEditable(false);
        Log.setBackground(new java.awt.Color(51, 51, 51));
        Log.setColumns(20);
        Log.setFont(Log.getFont().deriveFont(Log.getFont().getSize() - 1f));
        Log.setForeground(new java.awt.Color(0, 204, 0));
        Log.setLineWrap(true);
        Log.setRows(3);
        Log.setTabSize(4);
        Log.setText("Logging enable");
        Log.setToolTipText("Log");
        Log.setWrapStyleWord(true);
        Log.setAlignmentX(0.0F);
        Log.setAlignmentY(0.0F);
        Log.setBorder(Terminal.getBorder());
        Log.setComponentPopupMenu(contextMenuLog);
        Log.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Log.setMargin(new java.awt.Insets(0, 0, 0, 0));
        Log.setName("Log"); // NOI18N
        Log.setSelectionColor(new java.awt.Color(204, 0, 0));
        Log.setSelectionEnd(0);
        Log.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
            }
        });
        ScrollLog.setViewportView(Log);
        Log.getAccessibleContext().setAccessibleName("Log");

        RightSplitPane.setBottomComponent(ScrollLog);

        GroupLayout TerminalLogPaneLayout = new GroupLayout(terminalLogPane);
        terminalLogPane.setLayout(TerminalLogPaneLayout);
        TerminalLogPaneLayout.setHorizontalGroup(
                TerminalLogPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 299, Short.MAX_VALUE)
                        .addGroup(TerminalLogPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(RightSplitPane, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
        );
        TerminalLogPaneLayout.setVerticalGroup(
                TerminalLogPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 425, Short.MAX_VALUE)
                        .addGroup(TerminalLogPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(RightSplitPane, GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE))
        );
        terminalLogPane.setLayer(RightSplitPane, JLayeredPane.DEFAULT_LAYER);

        RightFilesSplitPane.setLeftComponent(terminalLogPane);

        FileManagerScrollPane.setBorder(null);
        FileManagerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        FileManagerScrollPane.setMaximumSize(new java.awt.Dimension(150, 150));
        FileManagerScrollPane.setPreferredSize(new java.awt.Dimension(150, 150));

        FileManagerPane.setMaximumSize(new java.awt.Dimension(145, 145));
        FileManagerPane.setName(""); // NOI18N
        FileManagerPane.setPreferredSize(new java.awt.Dimension(145, 145));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 2, 2);
        flowLayout1.setAlignOnBaseline(true);
        FileManagerPane.setLayout(flowLayout1);

        FileFormat.setIcon(new ImageIcon(getClass().getResource("/resources/file manager (delete).png"))); // NOI18N
        FileFormat.setText("Format");
        FileFormat.setToolTipText("Format (erase) NodeMCU file system. All files will be removed!");
        FileFormat.setHorizontalAlignment(SwingConstants.LEFT);
        FileFormat.setMargin(new java.awt.Insets(2, 4, 2, 4));
        FileFormat.setMaximumSize(new java.awt.Dimension(130, 25));
        FileFormat.setMinimumSize(new java.awt.Dimension(130, 25));
        FileFormat.setPreferredSize(new java.awt.Dimension(130, 25));
        FileFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileFormatActionPerformed();
            }
        });
        FileManagerPane.add(FileFormat);

        FileSystemInfo.setIcon(new ImageIcon(getClass().getResource("/resources/file manager.png"))); // NOI18N
        FileSystemInfo.setText("FS Info");
        FileSystemInfo.setToolTipText("Execute command file.fsinfo() and show total, used and remainig space on the ESP filesystem");
        FileSystemInfo.setAlignmentX(0.5F);
        FileSystemInfo.setHorizontalAlignment(SwingConstants.LEFT);
        FileSystemInfo.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileSystemInfo.setMaximumSize(new java.awt.Dimension(130, 25));
        FileSystemInfo.setPreferredSize(new java.awt.Dimension(130, 25));
        FileSystemInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileSystemInfoActionPerformed();
            }
        });
        FileManagerPane.add(FileSystemInfo);

        FileListReload.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        FileListReload.setIcon(new ImageIcon(getClass().getResource("/resources/refresh3.png"))); // NOI18N
        FileListReload.setText("Reload");
        FileListReload.setAlignmentX(0.5F);
        FileListReload.setHorizontalAlignment(SwingConstants.LEFT);
        FileListReload.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileListReload.setMaximumSize(new java.awt.Dimension(130, 25));
        FileListReload.setPreferredSize(new java.awt.Dimension(130, 25));
        FileListReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileListReloadActionPerformed();
            }
        });
        FileManagerPane.add(FileListReload);

        FileAsButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        FileAsButton1.setIcon(new ImageIcon(getClass().getResource("/resources/file.png"))); // NOI18N
        FileAsButton1.setText("File 1");
        FileAsButton1.setToolTipText("Left click");
        FileAsButton1.setAlignmentX(0.5F);
        FileAsButton1.setComponentPopupMenu(contextMenuESPFileLUA);
        FileAsButton1.setHorizontalAlignment(SwingConstants.LEFT);
        FileAsButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileAsButton1.setMaximumSize(new java.awt.Dimension(130, 25));
        FileAsButton1.setPreferredSize(new java.awt.Dimension(130, 25));
        FileAsButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            }
        });
        FileAsButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileAsButton1ActionPerformed(evt);
            }
        });
        FileManagerPane.add(FileAsButton1);

        FileRenamePanel.setMaximumSize(new java.awt.Dimension(130, 45));
        FileRenamePanel.setMinimumSize(new java.awt.Dimension(130, 45));

        FileRenameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        FileRenameLabel.setText("Old file name");
        FileRenameLabel.setToolTipText("Input new file name and hit Enter to completed or press Reload for cancel");
        FileRenameLabel.setMaximumSize(new java.awt.Dimension(130, 14));
        FileRenameLabel.setMinimumSize(new java.awt.Dimension(130, 14));
        FileRenameLabel.setPreferredSize(new java.awt.Dimension(130, 14));

        FileRename.setText("NewFileName");
        FileRename.setToolTipText("Input new file name and hit Enter to completed or press Reload for cancel");
        FileRename.setMaximumSize(new java.awt.Dimension(130, 25));
        FileRename.setMinimumSize(new java.awt.Dimension(130, 25));
        FileRename.setPreferredSize(new java.awt.Dimension(130, 25));
        FileRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileRenameActionPerformed();
            }
        });

        GroupLayout FileRenamePanelLayout = new GroupLayout(FileRenamePanel);
        FileRenamePanel.setLayout(FileRenamePanelLayout);
        FileRenamePanelLayout.setHorizontalGroup(
                FileRenamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(FileRenamePanelLayout.createSequentialGroup()
                                .addGroup(FileRenamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(FileRename, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(FileRenameLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        FileRenamePanelLayout.setVerticalGroup(
                FileRenamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(FileRenamePanelLayout.createSequentialGroup()
                                .addComponent(FileRenameLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FileRename, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        FileRenamePanel.setLayer(FileRenameLabel, JLayeredPane.DEFAULT_LAYER);
        FileRenamePanel.setLayer(FileRename, JLayeredPane.DEFAULT_LAYER);

        FileManagerPane.add(FileRenamePanel);

        FileManagerScrollPane.setViewportView(FileManagerPane);

        RightFilesSplitPane.setRightComponent(FileManagerScrollPane);

        RightSnippetsPane.setLayout(new java.awt.FlowLayout());

        ButtonSnippet0.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet0.setMnemonic(KeyEvent.VK_BACK_QUOTE);
        ButtonSnippet0.setText("Snippet0");
        ButtonSnippet0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet0ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet0);

        ButtonSnippet1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet1.setMnemonic(KeyEvent.VK_1);
        ButtonSnippet1.setText("Snippet1");
        ButtonSnippet1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet1ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet1);

        ButtonSnippet2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet2.setMnemonic(KeyEvent.VK_2);
        ButtonSnippet2.setText("Snippet2");
        ButtonSnippet2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet2ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet2);

        ButtonSnippet3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet3.setMnemonic(KeyEvent.VK_3);
        ButtonSnippet3.setText("Snippet3");
        ButtonSnippet3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet3ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet3);

        ButtonSnippet4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet4.setMnemonic(KeyEvent.VK_4);
        ButtonSnippet4.setText("Snippet4");
        ButtonSnippet4.setToolTipText("");
        ButtonSnippet4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet4ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet4);

        ButtonSnippet5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet5.setMnemonic(KeyEvent.VK_5);
        ButtonSnippet5.setText("Snippet5");
        ButtonSnippet5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet5ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet5);

        ButtonSnippet6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet6.setMnemonic(KeyEvent.VK_6);
        ButtonSnippet6.setText("Snippet6");
        ButtonSnippet6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet6ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet6);

        ButtonSnippet7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet7.setMnemonic(KeyEvent.VK_7);
        ButtonSnippet7.setText("Snippet7");
        ButtonSnippet7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet7ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet7);

        ButtonSnippet8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet8.setMnemonic(KeyEvent.VK_8);
        ButtonSnippet8.setText("Snippet8");
        ButtonSnippet8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet8ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet8);

        ButtonSnippet9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet9.setMnemonic(KeyEvent.VK_9);
        ButtonSnippet9.setText("Snippet9");
        ButtonSnippet9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet9ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet9);

        ButtonSnippet10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet10.setMnemonic(KeyEvent.VK_0);
        ButtonSnippet10.setText("Snippet10");
        ButtonSnippet10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet10ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet10);

        ButtonSnippet11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet11.setMnemonic(KeyEvent.VK_MINUS);
        ButtonSnippet11.setText("Snippet11");
        ButtonSnippet11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet11ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet11);

        ButtonSnippet12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet12.setMnemonic(KeyEvent.VK_EQUALS);
        ButtonSnippet12.setText("Snippet12");
        ButtonSnippet12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet12ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet12);

        ButtonSnippet13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet13.setMnemonic(KeyEvent.VK_BACK_SLASH);
        ButtonSnippet13.setText("Snippet13");
        ButtonSnippet13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet13ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet13);

        ButtonSnippet14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet14.setMnemonic(KeyEvent.VK_BACK_SPACE);
        ButtonSnippet14.setText("Snippet14");
        ButtonSnippet14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet14ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet14);

        ButtonSnippet15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ButtonSnippet15.setText("Snippet15");
        ButtonSnippet15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSnippet15ActionPerformed();
            }
        });
        RightSnippetsPane.add(ButtonSnippet15);

        GroupLayout RightBigPaneLayout = new GroupLayout(rightBigPane);
        rightBigPane.setLayout(RightBigPaneLayout);
        RightBigPaneLayout.setHorizontalGroup(
                RightBigPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(RightFilesSplitPane)
                        .addComponent(RightSnippetsPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        RightBigPaneLayout.setVerticalGroup(
                RightBigPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(RightBigPaneLayout.createSequentialGroup()
                                .addComponent(RightFilesSplitPane)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RightSnippetsPane, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE))
        );
        rightBigPane.setLayer(RightFilesSplitPane, JLayeredPane.DEFAULT_LAYER);
        rightBigPane.setLayer(RightSnippetsPane, JLayeredPane.DEFAULT_LAYER);

        logo.setHorizontalAlignment(SwingConstants.RIGHT);
        logo.setIcon(new ImageIcon(getClass().getResource("/resources/ESP8266-96x96.png"))); // NOI18N
        logo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logo.setHorizontalTextPosition(SwingConstants.LEFT);
        logo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoMouseClicked();
            }
        });

        RightExtraButtons.setLayout(new java.awt.FlowLayout());

        nodeHeap.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        nodeHeap.setText("Heap");
        nodeHeap.setToolTipText("Return the remain HEAP size in bytes");
        nodeHeap.setMaximumSize(new java.awt.Dimension(87, 30));
        nodeHeap.setMinimumSize(new java.awt.Dimension(87, 30));
        nodeHeap.setPreferredSize(new java.awt.Dimension(87, 30));
        nodeHeap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NodeHeapActionPerformed();
            }
        });
        RightExtraButtons.add(nodeHeap);

        nodeInfo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        nodeInfo.setText("Chip Info");
        nodeInfo.setToolTipText("Return NodeMCU version, chipid, flashid, flash size, flash mode, flash speed");
        nodeInfo.setMaximumSize(new java.awt.Dimension(87, 30));
        nodeInfo.setMinimumSize(new java.awt.Dimension(87, 30));
        nodeInfo.setPreferredSize(new java.awt.Dimension(87, 30));
        nodeInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NodeInfoActionPerformed();
            }
        });
        RightExtraButtons.add(nodeInfo);

        nodeChipID.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        nodeChipID.setText("Chip ID");
        nodeChipID.setToolTipText("Return chip ID");
        nodeChipID.setMargin(new java.awt.Insets(2, 2, 2, 2));
        nodeChipID.setMaximumSize(new java.awt.Dimension(87, 30));
        nodeChipID.setMinimumSize(new java.awt.Dimension(87, 30));
        nodeChipID.setPreferredSize(new java.awt.Dimension(87, 30));
        nodeChipID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NodeChipIDActionPerformed();
            }
        });
        RightExtraButtons.add(nodeChipID);

        nodeFlashID.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        nodeFlashID.setText("Flash ID");
        nodeFlashID.setToolTipText("Return flash ID");
        nodeFlashID.setMargin(new java.awt.Insets(2, 2, 2, 2));
        nodeFlashID.setMaximumSize(new java.awt.Dimension(87, 30));
        nodeFlashID.setMinimumSize(new java.awt.Dimension(87, 30));
        nodeFlashID.setPreferredSize(new java.awt.Dimension(87, 30));
        nodeFlashID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NodeFlashIDActionPerformed();
            }
        });
        RightExtraButtons.add(nodeFlashID);

        NodeReset.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        NodeReset.setIcon(new ImageIcon(getClass().getResource("/resources/reset.png"))); // NOI18N
        NodeReset.setText("Reset");
        NodeReset.setToolTipText("Soft reset by command node.reset()");
        NodeReset.setMargin(new java.awt.Insets(2, 4, 2, 4));
        NodeReset.setMaximumSize(new java.awt.Dimension(87, 30));
        NodeReset.setMinimumSize(new java.awt.Dimension(87, 30));
        NodeReset.setPreferredSize(new java.awt.Dimension(87, 30));
        NodeReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NodeResetActionPerformed();
            }
        });
        RightExtraButtons.add(NodeReset);

        DonateSmall.setIcon(new ImageIcon(getClass().getResource("/resources/donate-small.gif"))); // NOI18N
        DonateSmall.setHorizontalTextPosition(SwingConstants.CENTER);
        DonateSmall.setMargin(new java.awt.Insets(2, 2, 2, 2));
        DonateSmall.setMaximumSize(new java.awt.Dimension(100, 35));
        DonateSmall.setMinimumSize(new java.awt.Dimension(100, 35));
        DonateSmall.setPreferredSize(new java.awt.Dimension(100, 35));
        DonateSmall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DonateSmallActionPerformed();
            }
        });

        GroupLayout RightBasePaneLayout = new GroupLayout(rightBasePane);
        rightBasePane.setLayout(RightBasePaneLayout);
        RightBasePaneLayout.setHorizontalGroup(
                RightBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                .addGroup(RightBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(rightBigPane)
                                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                                .addComponent(LEDPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(logo))
                                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                                .addGroup(RightBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                                                .addComponent(rightBottomPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(DonateSmall, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(RightExtraButtons, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        RightBasePaneLayout.setVerticalGroup(
                RightBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(RightBasePaneLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(RightBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(LEDPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(logo))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rightBigPane)
                                .addGap(5, 5, 5)
                                .addComponent(RightExtraButtons, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(RightBasePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(rightBottomPane)
                                        .addComponent(DonateSmall, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        rightBasePane.setLayer(LEDPanel, JLayeredPane.DEFAULT_LAYER);
        rightBasePane.setLayer(rightBottomPane, JLayeredPane.DEFAULT_LAYER);
        rightBasePane.setLayer(rightBigPane, JLayeredPane.DEFAULT_LAYER);
        rightBasePane.setLayer(logo, JLayeredPane.DEFAULT_LAYER);
        rightBasePane.setLayer(RightExtraButtons, JLayeredPane.DEFAULT_LAYER);
        rightBasePane.setLayer(DonateSmall, JLayeredPane.DEFAULT_LAYER);

        horizontSplit.setRightComponent(rightBasePane);

        mainMenuBar.setAlignmentX(0.0F);
        mainMenuBar.setName("MainMenu"); // NOI18N
        mainMenuBar.setPreferredSize(new java.awt.Dimension(300, 22));

        menuFile.setText("File");
        menuFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        MenuItemFileNew.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileNew.setIcon(new ImageIcon(getClass().getResource("/resources/document.png"))); // NOI18N
        MenuItemFileNew.setText("<html><u>N</u>ew");
        MenuItemFileNew.setToolTipText("File New");
        MenuItemFileNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemFileNewActionPerformed();
            }
        });
        menuFile.add(MenuItemFileNew);

        MenuItemFileOpen.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileOpen.setIcon(new ImageIcon(getClass().getResource("/resources/folder open.png"))); // NOI18N
        MenuItemFileOpen.setText("<html><u>O</u>pen from disk");
        MenuItemFileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemFileOpenActionPerformed();
            }
        });
        menuFile.add(MenuItemFileOpen);

        MenuItemFileReload.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileReload.setIcon(new ImageIcon(getClass().getResource("/resources/refresh.png"))); // NOI18N
        MenuItemFileReload.setText("<html><u>R</u>eload from disk");
        MenuItemFileReload.setToolTipText("Reload file from disk, if you use external editor");
        MenuItemFileReload.setEnabled(false);
        MenuItemFileReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemFileReloadActionPerformed();
            }
        });
        menuFile.add(MenuItemFileReload);

        MenuItemFileSave.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileSave.setIcon(new ImageIcon(getClass().getResource("/resources/save.png"))); // NOI18N
        MenuItemFileSave.setText("<html><u>S</u>ave to disk");
        MenuItemFileSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemFileSaveActionPerformed();
            }
        });
        menuFile.add(MenuItemFileSave);

        MenuItemFileSaveAs.setIcon(new ImageIcon(getClass().getResource("/resources/save.png"))); // NOI18N
        MenuItemFileSaveAs.setText("Save As.. to disk");
        MenuItemFileSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        menuFile.add(MenuItemFileSaveAs);

        MenuItemFileSaveAll.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileSaveAll.setIcon(new ImageIcon(getClass().getResource("/resources/save_all.png"))); // NOI18N
        MenuItemFileSaveAll.setText("<html>Save <u>A</u>ll to disk");
        MenuItemFileSaveAll.setEnabled(false);
        menuFile.add(MenuItemFileSaveAll);

        MenuItemFileClose.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemFileClose.setIcon(new ImageIcon(getClass().getResource("/resources/folder closed.png"))); // NOI18N
        MenuItemFileClose.setText("Close");
        MenuItemFileClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemFileCloseActionPerformed();
            }
        });
        menuFile.add(MenuItemFileClose);
        menuFile.add(jSeparatorFileMenu);

        MenuItemFileSaveESP.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        MenuItemFileSaveESP.setIcon(new ImageIcon(getClass().getResource("/resources/move.png"))); // NOI18N
        MenuItemFileSaveESP.setText("<html><u>S</u>ave to ESP");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, FileSaveESP, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), MenuItemFileSaveESP, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        MenuItemFileSaveESP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemFileSaveESPActionPerformed();
            }
        });
        menuFile.add(MenuItemFileSaveESP);

        jMenuItem4.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem4.setText("<html>Save <u>A</u>ll to ESP");
        jMenuItem4.setEnabled(false);
        menuFile.add(jMenuItem4);

        MenuItemFileSendESP.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        MenuItemFileSendESP.setIcon(new ImageIcon(getClass().getResource("/resources/script_send.png"))); // NOI18N
        MenuItemFileSendESP.setText("<html>S<u>e</u>nd to ESP");
        MenuItemFileSendESP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemFileSendESPActionPerformed();
            }
        });
        menuFile.add(MenuItemFileSendESP);

        jMenuItem7.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem7.setText("<html><u>U</u>pload file to ESP");
        jMenuItem7.setEnabled(false);
        menuFile.add(jMenuItem7);
        menuFile.add(jSeparator4);

        MenuItemFileDo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK));
        MenuItemFileDo.setIcon(new ImageIcon(getClass().getResource("/resources/play.png"))); // NOI18N
        MenuItemFileDo.setText("<html><u>D</u>oFile on ESP");
        MenuItemFileDo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemFileDoActionPerformed();
            }
        });
        menuFile.add(MenuItemFileDo);
        menuFile.add(jSeparator3);

        MenuItemFileRemoveESP.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
        MenuItemFileRemoveESP.setIcon(new ImageIcon(getClass().getResource("/resources/trash.png"))); // NOI18N
        MenuItemFileRemoveESP.setText("<html><u>R</u>emove from ESP");
        MenuItemFileRemoveESP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemFileRemoveESPActionPerformed();
            }
        });
        menuFile.add(MenuItemFileRemoveESP);
        menuFile.add(jSeparator2);

        menuItemFileExit.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        menuItemFileExit.setText("Exit");
        menuItemFileExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        menuFile.add(menuItemFileExit);

        mainMenuBar.add(menuFile);

        menuEdit.setText("Edit");

        MenuItemEditUndo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditUndo.setIcon(new ImageIcon(getClass().getResource("/resources/undo1.png"))); // NOI18N
        MenuItemEditUndo.setText("Undo");
        MenuItemEditUndo.setEnabled(false);
        MenuItemEditUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditUndoActionPerformed();
            }
        });
        menuEdit.add(MenuItemEditUndo);

        MenuItemEditRedo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        MenuItemEditRedo.setIcon(new ImageIcon(getClass().getResource("/resources/redo1.png"))); // NOI18N
        MenuItemEditRedo.setText("Redo");
        MenuItemEditRedo.setEnabled(false);
        MenuItemEditRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditRedoActionPerformed();
            }
        });
        menuEdit.add(MenuItemEditRedo);
        menuEdit.add(jSeparator7);

        MenuItemEditCut.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditCut.setIcon(new ImageIcon(getClass().getResource("/resources/cut.png"))); // NOI18N
        MenuItemEditCut.setText("Cut");
        MenuItemEditCut.setEnabled(false);
        MenuItemEditCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditCutActionPerformed();
            }
        });
        menuEdit.add(MenuItemEditCut);

        MenuItemEditCopy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditCopy.setIcon(new ImageIcon(getClass().getResource("/resources/copy.png"))); // NOI18N
        MenuItemEditCopy.setText("Copy");
        MenuItemEditCopy.setEnabled(false);
        MenuItemEditCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditCopyActionPerformed();
            }
        });
        menuEdit.add(MenuItemEditCopy);

        MenuItemEditPaste.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemEditPaste.setIcon(new ImageIcon(getClass().getResource("/resources/paste.png"))); // NOI18N
        MenuItemEditPaste.setText("Paste");
        MenuItemEditPaste.setToolTipText("");
        MenuItemEditPaste.setEnabled(false);
        MenuItemEditPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditPasteActionPerformed();
            }
        });
        menuEdit.add(MenuItemEditPaste);
        menuEdit.add(jSeparator5);

        MenuItemEditSendSelected.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.ALT_MASK));
        MenuItemEditSendSelected.setIcon(new ImageIcon(getClass().getResource("/resources/send_selected.png"))); // NOI18N
        MenuItemEditSendSelected.setText("<html>Send selected <u>B</u>lock to ESP");
        MenuItemEditSendSelected.setToolTipText("Send selected block to ESP");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, MenuItemEditorSendSelected, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), MenuItemEditSendSelected, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        MenuItemEditSendSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditSendSelectedActionPerformed();
            }
        });
        menuEdit.add(MenuItemEditSendSelected);

        MenuItemEditSendLine.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK));
        MenuItemEditSendLine.setIcon(new ImageIcon(getClass().getResource("/resources/run_line.png"))); // NOI18N
        MenuItemEditSendLine.setText("<html>Send current <u>L</u>ine to ESP");
        MenuItemEditSendLine.setToolTipText("Send current line from code editor window to ESP");
        MenuItemEditSendLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEditSendLineActionPerformed();
            }
        });
        menuEdit.add(MenuItemEditSendLine);

        mainMenuBar.add(menuEdit);

        menuESP.setText("ESP");
        menuESP.setToolTipText("");

        MenuItemESPReset.setIcon(new ImageIcon(getClass().getResource("/resources/reset.png"))); // NOI18N
        MenuItemESPReset.setText("Restart ESP module");
        MenuItemESPReset.setToolTipText("Send RESET command (firmware depended)");
        MenuItemESPReset.setEnabled(false);
        MenuItemESPReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemESPResetActionPerformed();
            }
        });
        menuESP.add(MenuItemESPReset);

        MenuItemESPFormat.setIcon(new ImageIcon(getClass().getResource("/resources/file manager (delete).png"))); // NOI18N
        MenuItemESPFormat.setText("Format ESP");
        MenuItemESPFormat.setToolTipText("Remove All files from ESP flash memory");
        MenuItemESPFormat.setEnabled(false);
        MenuItemESPFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemESPFormatActionPerformed();
            }
        });
        menuESP.add(MenuItemESPFormat);

        mainMenuBar.add(menuESP);

        MenuView.setText("View");
        buttonGroupLF.add(MenuView);
        MenuView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        AlwaysOnTop.setText("Always On Top");
        AlwaysOnTop.setToolTipText("");
        AlwaysOnTop.setIcon(new ImageIcon(getClass().getResource("/resources/AlwaysOnTop.png"))); // NOI18N
        AlwaysOnTop.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                AlwaysOnTopItemStateChanged();
            }
        });
        MenuView.add(AlwaysOnTop);

        MenuItemViewLog.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemViewLog.setSelected(true);
        MenuItemViewLog.setText("<html>Show <u>L</u>og");
        MenuItemViewLog.setToolTipText("Enable/disable log window");
        MenuItemViewLog.setIcon(new ImageIcon(getClass().getResource("/resources/backup.png"))); // NOI18N
        MenuItemViewLog.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MenuItemViewLogItemStateChanged();
            }
        });
        MenuItemViewLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewLogActionPerformed();
            }
        });
        MenuView.add(MenuItemViewLog);

        menuItemViewClearLog.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.CTRL_MASK));
        menuItemViewClearLog.setIcon(new ImageIcon(getClass().getResource("/resources/backup_delete.png"))); // NOI18N
        menuItemViewClearLog.setText("Clear log");
        menuItemViewClearLog.setToolTipText("Clear log window");
        menuItemViewClearLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewClearLogActionPerformed();
            }
        });
        MenuView.add(menuItemViewClearLog);

        menuItemViewClearTerminal.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.CTRL_MASK));
        menuItemViewClearTerminal.setIcon(new ImageIcon(getClass().getResource("/resources/terminal_clear.png"))); // NOI18N
        menuItemViewClearTerminal.setText("Clear terminal");
        menuItemViewClearTerminal.setToolTipText("Clear terminal window");
        menuItemViewClearTerminal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewClearTerminalActionPerformed();
            }
        });
        MenuView.add(menuItemViewClearTerminal);
        MenuView.add(jSeparator9);

        MenuItemViewToolbar.setSelected(true);
        MenuItemViewToolbar.setText("Show toolbar at top left");
        MenuItemViewToolbar.setToolTipText("Enable/disable files toolbar at top left");
        MenuItemViewToolbar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MenuItemViewToolbarItemStateChanged();
            }
        });
        MenuItemViewToolbar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewToolbarActionPerformed();
            }
        });
        MenuView.add(MenuItemViewToolbar);

        MenuItemViewLeftExtra.setText("Show Extra buttons at lleft");
        MenuItemViewLeftExtra.setToolTipText("Enable/disable Extra buttons panel at left");
        MenuItemViewLeftExtra.setEnabled(false);
        MenuItemViewLeftExtra.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MenuItemViewLeftExtraItemStateChanged();
            }
        });
        MenuItemViewLeftExtra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewLeftExtraActionPerformed();
            }
        });
        MenuView.add(MenuItemViewLeftExtra);

        MenuItemViewSnippets.setSelected(true);
        MenuItemViewSnippets.setText("Show Snippets panel at right");
        MenuItemViewSnippets.setToolTipText("Enable/disable Snippets panel");
        MenuItemViewSnippets.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MenuItemViewSnippetsItemStateChanged();
            }
        });
        MenuItemViewSnippets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewSnippetsActionPerformed();
            }
        });
        MenuView.add(MenuItemViewSnippets);

        MenuItemViewFileManager.setSelected(true);
        MenuItemViewFileManager.setText("Show FileManager panel at right");
        MenuItemViewFileManager.setToolTipText("Enable/disable FileManager panel at right");
        MenuItemViewFileManager.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MenuItemViewFileManagerItemStateChanged();
            }
        });
        MenuItemViewFileManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewFileManagerActionPerformed();
            }
        });
        MenuView.add(MenuItemViewFileManager);

        MenuItemViewRightExtra.setSelected(true);
        MenuItemViewRightExtra.setText("Show Extra buttons at bottom right");
        MenuItemViewRightExtra.setToolTipText("Enable/disable Extra buttons panel at bottom right");
        MenuItemViewRightExtra.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MenuItemViewRightExtraItemStateChanged();
            }
        });
        MenuItemViewRightExtra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewRightExtraActionPerformed();
            }
        });
        MenuView.add(MenuItemViewRightExtra);

        MenuItemViewDonate.setSelected(true);
        MenuItemViewDonate.setText("<html>I'm already make donation, <br />please hide dontation button at bottom right!");
        MenuItemViewDonate.setToolTipText("Enable/disable Extra buttons panel at bottom right");
        MenuItemViewDonate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MenuItemViewDonateItemStateChanged();
            }
        });
        MenuItemViewDonate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        MenuView.add(MenuItemViewDonate);
        MenuView.add(jSeparator13);

        MenuItemViewTermFontInc.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemViewTermFontInc.setIcon(new ImageIcon(getClass().getResource("/resources/zoom in.png"))); // NOI18N
        MenuItemViewTermFontInc.setText("Terminal font size inc");
        MenuItemViewTermFontInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewTermFontIncActionPerformed();
            }
        });
        MenuView.add(MenuItemViewTermFontInc);

        MenuItemViewTermFontDec.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemViewTermFontDec.setIcon(new ImageIcon(getClass().getResource("/resources/zoom out.png"))); // NOI18N
        MenuItemViewTermFontDec.setText("Terminal font size dec");
        MenuItemViewTermFontDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewTermFontDecActionPerformed();
            }
        });
        MenuView.add(MenuItemViewTermFontDec);
        MenuView.add(jSeparator10);

        MenuItemViewEditorFontInc.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, java.awt.event.InputEvent.ALT_MASK));
        MenuItemViewEditorFontInc.setIcon(new ImageIcon(getClass().getResource("/resources/zoom in.png"))); // NOI18N
        MenuItemViewEditorFontInc.setText("Editor font size inc");
        MenuItemViewEditorFontInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewEditorFontIncActionPerformed();
            }
        });
        MenuView.add(MenuItemViewEditorFontInc);

        MenuItemViewEditorFontDec.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.ALT_MASK));
        MenuItemViewEditorFontDec.setIcon(new ImageIcon(getClass().getResource("/resources/zoom out.png"))); // NOI18N
        MenuItemViewEditorFontDec.setText("Editor font size dec");
        MenuItemViewEditorFontDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewEditorFontDecActionPerformed();
            }
        });
        MenuView.add(MenuItemViewEditorFontDec);
        MenuView.add(jSeparator11);

        MenuItemViewLogFontInc.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MenuItemViewLogFontInc.setIcon(new ImageIcon(getClass().getResource("/resources/zoom in.png"))); // NOI18N
        MenuItemViewLogFontInc.setText("Log font size inc");
        MenuItemViewLogFontInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewLogFontIncActionPerformed();
            }
        });
        MenuView.add(MenuItemViewLogFontInc);

        MenuItemViewLogFontDec.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MenuItemViewLogFontDec.setIcon(new ImageIcon(getClass().getResource("/resources/zoom out.png"))); // NOI18N
        MenuItemViewLogFontDec.setText("Log font size dec");
        MenuItemViewLogFontDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewLogFontDecActionPerformed();
            }
        });
        MenuView.add(MenuItemViewLogFontDec);
        MenuView.add(jSeparator12);

        menuItemViewFontDefault.setText("Reset all font size to default");
        menuItemViewFontDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewFontDefaultActionPerformed();
            }
        });
        MenuView.add(menuItemViewFontDefault);
        MenuView.add(jSeparator17);

        buttonGroupLF.add(MenuItemViewLF1);
        MenuItemViewLF1.setText("Nimbus");
        MenuItemViewLF1.setToolTipText("");
        MenuItemViewLF1.setActionCommand("0");
        MenuItemViewLF1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemViewLF1ActionPerformed(evt);
            }
        });
        MenuView.add(MenuItemViewLF1);

        mainMenuBar.add(MenuView);

        menuLinks.setText("Links");
        menuLinks.setToolTipText("");

        menuItemLinksAPIcn.setText("NodeMCU API cn");
        menuItemLinksAPIcn.setToolTipText("Open doc NodeMCU API Chinese in browser");
        menuItemLinksAPIcn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksAPIcnActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksAPIcn);

        menuItemLinksAPIen.setText("NodeMCU API en");
        menuItemLinksAPIen.setToolTipText("Open doc NodeMCU API English in browser");
        menuItemLinksAPIen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksAPIenActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksAPIen);

        menuItemLinksAPIru.setText("NodeMCU API ru");
        menuItemLinksAPIru.setToolTipText("Open doc NodeMCU API Russian in browser");
        menuItemLinksAPIru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksAPIruActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksAPIru);

        menuItemLinksChangelog.setText("NodeMCU changelog");
        menuItemLinksChangelog.setToolTipText("Open NodeMCU changelog in browser");
        menuItemLinksChangelog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksChangelogActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksChangelog);
        menuLinks.add(jSeparator14);

        menuItemLinksDownloadLatestFirmware.setText("NodeMCU download latest firmware");
        menuItemLinksDownloadLatestFirmware.setToolTipText("Download NodeMCU latest firmware (stable version)");
        menuItemLinksDownloadLatestFirmware.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksDownloadLatestFirmwareActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksDownloadLatestFirmware);

        menuItemLinksDownloadLatestDev.setText("NodeMCU download latest dev firmware");
        menuItemLinksDownloadLatestDev.setToolTipText("Download NodeMCU latest firmware (dev version)");
        menuItemLinksDownloadLatestDev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksDownloadLatestDevActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksDownloadLatestDev);

        menuItemLinksDownloadLatestFlasher.setText("NodeMCU download latest FLASHER");
        menuItemLinksDownloadLatestFlasher.setToolTipText("Download latest version of NodeMCU Flasher x32 or x64");
        menuItemLinksDownloadLatestFlasher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksDownloadLatestFlasherActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksDownloadLatestFlasher);
        menuLinks.add(jSeparator15);

        menuItemLinksBuyDevBoard.setText("Buy NodeMCU dev board");
        menuItemLinksBuyDevBoard.setToolTipText("Buy NodeMCU development boards on Aliexpress");
        menuItemLinksBuyDevBoard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksBuyDevBoardActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksBuyDevBoard);

        menuItemLinksBuyESP8266.setText("Buy ESP8266 ESP-01 - ESP12 modules");
        menuItemLinksBuyESP8266.setToolTipText("Buy ESP8266 ESP-01 ESP-02 ... ESP-12 modules on Aliexpress");
        menuItemLinksBuyESP8266.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksBuyESP8266ActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksBuyESP8266);

        menuItemLinksBuyESD12.setText("Buy ESP8266 ESD-12 4M module");
        menuItemLinksBuyESD12.setToolTipText("Buy ESP8266 ESD-12 4M flash board");
        menuItemLinksBuyESD12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksBuyESD12ActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksBuyESD12);

        menuItemLinksBuyOther.setText("Buy other usefull parts");
        menuItemLinksBuyOther.setToolTipText("Buy any electronics and other on Aliexpress");
        menuItemLinksBuyOther.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksBuyOtherActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksBuyOther);
        menuLinks.add(jSeparator16);

        menuItemLinksESPlorerForumEn.setText("ESPlorer discuss, bug report en");
        menuItemLinksESPlorerForumEn.setToolTipText("Link to ESP8266.COM forum, ESPlorer topic, English");
        menuItemLinksESPlorerForumEn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksESPlorerForumEnActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksESPlorerForumEn);

        menuItemLinksESPlorerForumRu.setText("ESPlorer discuss, bug report ru");
        menuItemLinksESPlorerForumRu.setToolTipText("Link to ESP8266.RU forum, ESPlorer topic, Russian");
        menuItemLinksESPlorerForumRu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksESPlorerForumRuActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksESPlorerForumRu);

        menuItemLinksESPlorerLatest.setText("ESPlorer download latest stable");
        menuItemLinksESPlorerLatest.setToolTipText("Link to ESP8266.RU, download ESPlorer latest");
        menuItemLinksESPlorerLatest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksESPlorerLatestActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksESPlorerLatest);

        menuItemLinksESPlorerSource.setText("ESPlorer download source code from github");
        menuItemLinksESPlorerSource.setToolTipText("Link to GITHUB for download ESPlorer source code");
        menuItemLinksESPlorerSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksESPlorerSourceActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksESPlorerSource);

        menuItemLinksESPlorerHome.setText("ESPlorer home page on esp8266.ru");
        menuItemLinksESPlorerHome.setToolTipText("Link to ESP8266.RU, ESPlorer HomePage");
        menuItemLinksESPlorerHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksESPlorerHomeActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksESPlorerHome);

        menuItemLinksDonate.setText("Make donation for ESPlorer developer");
        menuItemLinksDonate.setToolTipText("You can make donation for ESPlorer developer");
        menuItemLinksDonate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLinksDonateActionPerformed();
            }
        });
        menuLinks.add(menuItemLinksDonate);

        mainMenuBar.add(menuLinks);

        menuHelp.setText("?");

        MenuItemHelpAbout.setText("aboutDialog");
        MenuItemHelpAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemHelpAboutActionPerformed();
            }
        });
        menuHelp.add(MenuItemHelpAbout);

        mainMenuBar.add(menuHelp);

        setJMenuBar(mainMenuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(horizontSplit, GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(horizontSplit, GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleDescription("");

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SpeedActionPerformed() {//GEN-FIRST:event_SpeedActionPerformed
        if (portJustOpen) {
            log("ERROR: Communication with MCU not established.");
            return;
        }
        nSpeed = Integer.parseInt((String) Speed.getSelectedItem());
        if (pOpen) { // reconnect
            if (OptionNodeMCU.isSelected()) {
                log("Try to reconnect with baud " + Integer.toString(nSpeed) + "...");
                btnSend("print(uart.setup(0, " + nSpeed + ", 8, 0, 1, 1 ))");
                try {
                    Thread.sleep(200L);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getMessage());
                }
            } else {
                return;
            }
            if (SetSerialPortParams()) {
                log("Reconnect: Success.");
                CheckComm();
            }
        }
        UpdateLED();
    }//GEN-LAST:event_SpeedActionPerformed

    private void OpenActionPerformed() {//GEN-FIRST:event_OpenActionPerformed
        if (Open.isSelected()) {
            if (pOpen) {
                return;
            }
            try {
                String port;
                if (UseCustomPortName.isSelected()) {
                    port = CustomPortName.getText().trim();
                } else {
                    port = Port.getSelectedItem().toString().trim();
                }
                prefs.put(Constants.SERIAL_PORT, port);
                log("Serial port " + port + " save as default.");
                int speed = Speed.getSelectedIndex();
                prefs.putInt(Constants.SERIAL_BAUD, speed);
                log("Baud rate " + Speed.getSelectedItem().toString().trim() + " save as default.");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
            pOpen = portOpen();
            Open.setSelected(pOpen);
        } else {
            portClose();
        }
        UpdateButtons();
    }//GEN-LAST:event_OpenActionPerformed

    private void UpdateButtons() {
        pOpen = Open.isSelected();
        if (pOpen && !portJustOpen) {
            UpdateLED();
            Port.setEnabled(false);
            //Speed.setEnabled(false);
            ReScan.setEnabled(false);
            SendCommand.setEnabled(true);
            Command.setEnabled(true);
            CR.setEnabled(true);
            LF.setEnabled(true);
            // left panel
            FileSaveESP.setEnabled(true);
            MenuItemFileSaveESP.setEnabled(true);
            FileSendESP.setEnabled(true);
            MenuItemFileSendESP.setEnabled(true);
            MenuItemFileRemoveESP.setEnabled(true);
            FileDo.setEnabled(true);
            MenuItemFileDo.setEnabled(true);
            MenuItemEditorSendLine.setEnabled(true);
            MenuItemEditSendLine.setEnabled(true);
            ButtonSendLine.setEnabled(true);
            NodeReset.setEnabled(true);
            MenuItemTerminalReset.setEnabled(true);
            MenuItemTerminalFormat.setEnabled(true);
            MenuItemESPReset.setEnabled(true);
            MenuItemESPFormat.setEnabled(true);
            SnippetRun.setEnabled(true);
            ButtonSendLine.setEnabled(true);
            ButtonSnippet0.setEnabled(true);
            ButtonSnippet1.setEnabled(true);
            ButtonSnippet2.setEnabled(true);
            ButtonSnippet3.setEnabled(true);
            ButtonSnippet4.setEnabled(true);
            ButtonSnippet5.setEnabled(true);
            ButtonSnippet6.setEnabled(true);
            ButtonSnippet7.setEnabled(true);
            ButtonSnippet8.setEnabled(true);
            ButtonSnippet9.setEnabled(true);
            ButtonSnippet10.setEnabled(true);
            ButtonSnippet11.setEnabled(true);
            ButtonSnippet12.setEnabled(true);
            ButtonSnippet13.setEnabled(true);
            ButtonSnippet14.setEnabled(true);
            ButtonSnippet15.setEnabled(true);

        } else {
            UpdateLED();
            Port.setEnabled(true);
            //Speed.setEnabled(true);
            ReScan.setEnabled(true);
            SendCommand.setEnabled(false);
            Command.setEnabled(false);
            CR.setEnabled(false);
            LF.setEnabled(false);
            // left panel
            FileSaveESP.setEnabled(false);
            FileSaveESP.setSelected(false);
            MenuItemFileSaveESP.setEnabled(false);
            FileSendESP.setEnabled(false);
            FileSendESP.setSelected(false);
            MenuItemFileSendESP.setEnabled(false);
            MenuItemFileRemoveESP.setEnabled(false);
            FileDo.setEnabled(false);
            MenuItemFileDo.setEnabled(false);
            MenuItemEditorSendLine.setEnabled(false);
            MenuItemEditSendLine.setEnabled(false);
            ButtonSendLine.setEnabled(false);
            NodeReset.setEnabled(false);
            MenuItemTerminalReset.setEnabled(false);
            MenuItemTerminalFormat.setEnabled(false);
            MenuItemESPReset.setEnabled(false);
            MenuItemESPFormat.setEnabled(false);
            SnippetRun.setEnabled(false);
            ButtonSnippet0.setEnabled(false);
            ButtonSnippet1.setEnabled(false);
            ButtonSnippet2.setEnabled(false);
            ButtonSnippet3.setEnabled(false);
            ButtonSnippet4.setEnabled(false);
            ButtonSnippet5.setEnabled(false);
            ButtonSnippet6.setEnabled(false);
            ButtonSnippet7.setEnabled(false);
            ButtonSnippet8.setEnabled(false);
            ButtonSnippet9.setEnabled(false);
            ButtonSnippet10.setEnabled(false);
            ButtonSnippet11.setEnabled(false);
            ButtonSnippet12.setEnabled(false);
            ButtonSnippet13.setEnabled(false);
            ButtonSnippet14.setEnabled(false);
            ButtonSnippet15.setEnabled(false);
        }
    }

    private void formWindowOpened() {//GEN-FIRST:event_formWindowOpened
        PortFinder();
        this.setTitle("ESPlorer");
        ProgressBar.setVisible(false);
        CommandsSetNodeMCU();
        isToolbarShow();
        isLeftExtraShow();
        isRightExtraShow();
        isRightSnippetsShow();
        SetWindowSize();
        isFileManagerShow();
        isLogShow();
    }//GEN-LAST:event_formWindowOpened

    private void SetWindowSize() {
        int x, y, h, w;
        x = prefs.getInt(Constants.WIN_X, 0);
        y = prefs.getInt(Constants.WIN_Y, 0);
        h = prefs.getInt(Constants.WIN_H, 768);
        w = prefs.getInt(Constants.WIN_W, 1024);
        this.setBounds(x, y, w, h);
    }

    private void isToolbarShow() {
        FilesToolBar.setVisible(MenuItemViewToolbar.isSelected());
    }

    private void isLeftExtraShow() {
        LeftExtraButtons.setVisible(MenuItemViewLeftExtra.isSelected());
    }

    private void isRightExtraShow() {
        RightExtraButtons.setVisible(MenuItemViewRightExtra.isSelected());
    }

    private void isRightSnippetsShow() {
        RightSnippetsPane.setVisible(MenuItemViewSnippets.isSelected());
    }

    private void isFileManagerShow() {
        int div;
        final int w = 160;
        if (MenuItemViewFileManager.isSelected()) {
            FileManagerScrollPane.setEnabled(true);
            FileManagerScrollPane.setVisible(true);
            //div = prefs.getInt( Constants.FM_DIV, RightFilesSplitPane.getWidth()-w );
            //if ( div > RightFilesSplitPane.getWidth()-w ) {
            div = RightFilesSplitPane.getWidth() - w;
            //}
            RightFilesSplitPane.setDividerLocation(div);
        } else {
            FileManagerScrollPane.setEnabled(false);
            FileManagerScrollPane.setVisible(false);
            RightFilesSplitPane.setDividerLocation(RightFilesSplitPane.getWidth() - RightFilesSplitPane.getDividerSize());
        }
    }

    private void isLogShow() {
        if (MenuItemViewLog.isSelected()) {
            ScrollLog.setVisible(true);
            ScrollLog.setEnabled(true);
            RightSplitPane.setDividerLocation(prefs.getInt(Constants.LOG_DIV, RightSplitPane.getHeight() - 200));
        } else {
            ScrollLog.setVisible(false);
            ScrollLog.setEnabled(false);
            RightSplitPane.setDividerLocation(RightSplitPane.getHeight() - RightSplitPane.getDividerSize());
        }
    }

    private void CommandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CommandActionPerformed
        //log("CommandActionPerformed " + evt.getActionCommand());
        if ("comboBoxEdited".equals(evt.getActionCommand())) { // Hit Enter
            SendCommand.doClick();
        }
    }//GEN-LAST:event_CommandActionPerformed


    private void cmdSetCIPSTARTActionPerformed() {//GEN-FIRST:event_cmdSetCIPSTARTActionPerformed
        String cmd = "AT+CIPSTART=";
        if (multi.isSelected()) {
            cmd += conn_id.getSelectedItem().toString() + ",";
            btnSend("AT+CIPMUX=1");
        } else {
            btnSend("AT+CIPMUX=0");
        }
        if (protocol.getSelectedIndex() == 0) { // TCP
            cmd += "\"TCP\",\"";
        } else {
            cmd += "\"UDP\",\"";
        }
        cmd += remote_address.getText().trim() + "\"," + remote_port.getText().trim();
        if ((udp_local_port.getText().trim().isEmpty()) && (protocol.getSelectedIndex() == 1)) {
            cmd += "," + udp_local_port.getText().trim() + "," + udp_mode.getText().trim();
        }
        btnSend(cmd);
    }//GEN-LAST:event_cmdSetCIPSTARTActionPerformed


    private void cmdCIPSENDActionPerformed() {//GEN-FIRST:event_cmdCIPSENDActionPerformed
        String cmd = "AT+CIPSEND=";
        String len = Integer.toString(data.getText().length());
        if (multi.isSelected()) {
            cmd += conn_id.getSelectedItem().toString() + ",";
        }
        cmd += len;
        btnSend(cmd);
        btnSend(data.getText());
    }//GEN-LAST:event_cmdCIPSENDActionPerformed

    private void cmdSetCIPCLOSEActionPerformed() {//GEN-FIRST:event_cmdSetCIPCLOSEActionPerformed
        String cmd = "AT+CIPCLOSE";
        if (multi.isSelected()) {
            cmd += "=" + conn_id.getSelectedItem().toString();
        }
        btnSend(cmd);
    }//GEN-LAST:event_cmdSetCIPCLOSEActionPerformed

    private void ButtonFileNewActionPerformed() {//GEN-FIRST:event_ButtonFileNewActionPerformed
        MenuItemFileNew.doClick();
    }//GEN-LAST:event_ButtonFileNewActionPerformed

    private void ContextMenuTerminalPopupMenuWillBecomeVisible() {//GEN-FIRST:event_ContextMenuTerminalPopupMenuWillBecomeVisible
        try {
            MenuItemTerminalCopy.setEnabled(Terminal.getSelectedText().length() > 0);
        } catch (Exception e) {
            MenuItemTerminalCopy.setEnabled(false);
        }
        int size = Terminal.getFont().getSize();
        String inc, dec;
        if (size < Constants.TERMINAL_FONT_SIZE_MAX) {
            inc = "Change font size from " + Integer.toString(size) + " to " + Integer.toString(size + 1);
        } else {
            inc = "Set font size to " + Float.toString(Constants.TERMINAL_FONT_SIZE_MIN);
        }
        MenuItemTerminalFontInc.setText(inc);
        if (size > Constants.TERMINAL_FONT_SIZE_MIN) {
            dec = "Change font size from " + Integer.toString(size) + " to " + Integer.toString(size - 1);
        } else {
            dec = "Set font size to " + Float.toString(Constants.TERMINAL_FONT_SIZE_MAX);
        }
        MenuItemTerminalFontDec.setText(dec);
    }//GEN-LAST:event_ContextMenuTerminalPopupMenuWillBecomeVisible


    private void PASSFocusGained() {//GEN-FIRST:event_PASSFocusGained
        if (PASS.getText().trim().equals("password")) {
            PASS.setText("");
        }
    }//GEN-LAST:event_PASSFocusGained

    private void SSIDFocusGained() {//GEN-FIRST:event_SSIDFocusGained
        if (SSID.getText().trim().equals("SSID")) {
            SSID.setText("");
        }
    }//GEN-LAST:event_SSIDFocusGained

    private void MenuItemViewLogActionPerformed() {//GEN-FIRST:event_MenuItemViewLogActionPerformed
        isLogShow();
        prefs.putBoolean(Constants.SHOW_LOG, MenuItemViewLog.isSelected());
    }//GEN-LAST:event_MenuItemViewLogActionPerformed

    private void MenuItemFileSaveActionPerformed() {//GEN-FIRST:event_MenuItemFileSaveActionPerformed
        SaveFile();
        if (FileAutoSaveESP.isSelected()) {
            SaveFileESP();
        }
    }

    private boolean isFileNew() {
        try {
            if (FilesTabbedPane.getTitleAt(iTab).equals(NewFile)) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    private boolean SaveFile() {
        boolean success = false;
        if (isFileNew()) { // we saving new file
            log("Saving new file...");
            FileCount++;
            iFile.set(iTab, new File("script" + Integer.toString(FileCount) + ".lua"));
            chooser.rescanCurrentDirectory();
            chooser.setSelectedFile(iFile.get(iTab));
            int returnVal = chooser.showSaveDialog(null);
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                log("Saving abort by user.");
                UpdateEditorButtons();
                return false;
            }
            SavePath();
            iFile.set(iTab, chooser.getSelectedFile());
            if (iFile.get(iTab).exists()) {
                log("File " + iFile.get(iTab).getName() + " already exist, waiting user choice");
                int shouldWrite = Dialog("File " + iFile.get(iTab).getName() + " already exist. Overwrite?", JOptionPane.YES_NO_OPTION);
                if (shouldWrite != JOptionPane.YES_OPTION) {
                    UpdateEditorButtons();
                    log("Saving canceled by user, because file " + FileName + " already exist");
                    return false;
                } else {
                    log("File " + FileName + " will be overwriten by user choice");
                }
            }
        } else { // we saving file, when open
            log("We save known file " + iFile.get(iTab).getName());
        }
        try {
            log("Try to saving file " + iFile.get(iTab).getName() + " ...");
            fos = new FileOutputStream(iFile.get(iTab));
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write(TextEditor1.get(iTab).getText());
            bw.flush();
            osw.flush();
            fos.flush();
            FileName = iFile.get(iTab).getName();
            log("Save file " + FileName + ": Success.");
            FilesTabbedPane.setTitleAt(iTab, FileName);
            UpdateEditorButtons();
            success = true;
        } catch (IOException ex) {
            log("Save file " + iFile.get(iTab).getName() + ": FAIL.");
            log(ex.toString());
//            log(ex.getStackTrace().toString());
            JOptionPane.showMessageDialog(null, "Error, file not saved!");
        }
        try {
            if (bw != null) bw.close();
            if (osw != null) osw.close();
            if (fos != null) fos.close();
        } catch (IOException ex) {
            log(ex.toString());
//            log(ex.getStackTrace().toString());
        }
        TextEditor1.get(iTab).discardAllEdits();
        FileChanged.set(iTab, false);
        UpdateEditorButtons();
        return success;
    }

    private void MenuItemFileNewActionPerformed() {//GEN-FIRST:event_MenuItemFileNewActionPerformed
        FileNew("");
    }//GEN-LAST:event_MenuItemFileNewActionPerformed

    private void FileNew(String s) {
        if (UseExternalEditor.isSelected()) {
            return;
        }
        AddTab(s);
        if (s.isEmpty()) {
            log("New empty file ready.");
        } else {
            log("New file ready, content load: Success.");
        }
    }

    private void MenuItemEditCutActionPerformed() {//GEN-FIRST:event_MenuItemEditCutActionPerformed
        if (UseExternalEditor.isSelected()) {
            return;
        }
        TextEditor1.get(iTab).cut();
        FileChanged.set(iTab, true);
    }//GEN-LAST:event_MenuItemEditCutActionPerformed

    private void MenuItemEditCopyActionPerformed() {//GEN-FIRST:event_MenuItemEditCopyActionPerformed
        TextEditor1.get(iTab).copy();
    }//GEN-LAST:event_MenuItemEditCopyActionPerformed

    private void MenuItemEditPasteActionPerformed() {//GEN-FIRST:event_MenuItemEditPasteActionPerformed
        if (UseExternalEditor.isSelected()) {
            return;
        }
        TextEditor1.get(iTab).paste();
        FileChanged.set(iTab, true);
    }//GEN-LAST:event_MenuItemEditPasteActionPerformed

    // File open
    private void MenuItemFileOpenActionPerformed() {//GEN-FIRST:event_MenuItemFileOpenActionPerformed
        OpenFile();
    }//GEN-LAST:event_MenuItemFileOpenActionPerformed

    private void SavePath() {
        workDir = chooser.getCurrentDirectory().toString();
        prefs.put(Constants.PATH, workDir);
    }

    private void OpenFile() {
        chooser.rescanCurrentDirectory();
        int success = chooser.showOpenDialog(LeftBasePane);
        if (success == JFileChooser.APPROVE_OPTION) {
            SavePath();
            int isOpen = -1;
            for (int i = 0; i < iFile.size(); i++) {
                if (chooser.getSelectedFile().getPath().equals(iFile.get(i).getPath())) {
                    iTab = i;
                    isOpen = i;
                    break;
                }
            }
            if (isOpen >= 0) {
                FilesTabbedPane.setSelectedIndex(iTab);
                UpdateEditorButtons();
                FileName = chooser.getSelectedFile().getName();
                log("File " + FileName + " already open, select tab to file " + FileName);
                JOptionPane.showMessageDialog(null, "File " + FileName + " already open. You can use 'Reload' only.");
                return;
            }
            if (!isFileNew() || isChanged()) {
                AddTab("");
            }
            log("Try to open file " + chooser.getSelectedFile().getName());
            try {
                iFile.set(iTab, chooser.getSelectedFile());
                FileName = iFile.get(iTab).getName();
                log("File name: " + iFile.get(iTab).getPath());
                if (iFile.get(iTab).length() > 1024 * 1024) { // 1M
                    JOptionPane.showMessageDialog(null, "File " + FileName + " too large.");
                    log("File too large. Size: " + Long.toString(iFile.get(iTab).length() / 1024 / 1024) + " Mb, file: " + iFile.get(iTab).getPath());
                    UpdateEditorButtons();
                    return;
                }
                FilesTabbedPane.setTitleAt(iTab, iFile.get(iTab).getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error, file is not open!");
                log(ex.toString());
                log("Open: FAIL.");
//                log(ex.getStackTrace().toString());
            }
            if (LoadFile()) {
                log("Open \"" + FileName + "\": Success.");
            }
        }
        UpdateEditorButtons();
    }

    private boolean LoadFile() {
        if (isFileNew()) {
            UpdateEditorButtons();
            log("Internal error 101: FileTab is NewFile.");
            return false;
        }
        FileName = "";
        try {
            FileName = iFile.get(iTab).getName();
            log("Try to load file " + FileName);
        } catch (Exception e) {
            log("Internal error 102: no current file descriptor.");
            return false;
        }
        InputStreamReader isr;
        BufferedReader br;
        try {
            fis = new FileInputStream(iFile.get(iTab));
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            TextEditor1.get(iTab).setText(sb.toString());
        } catch (Exception ex) {
            log(ex.toString());
//                log(ex.getStackTrace().toString());
            log("Loading " + FileName + ": FAIL.");
            UpdateEditorButtons();
            JOptionPane.showMessageDialog(null, "Error, file not load!");
            return false;
        }
        try {
            br.close();
            isr.close();
            fis.close();
        } catch (Exception ex) {
            log(ex.toString());
//                log(ex.getStackTrace().toString());
            log("Internal error 103: can't close stream.");
        }
        TextEditor1.get(iTab).setCaretPosition(0);
        FileChanged.set(iTab, false);
        TextEditor1.get(iTab).discardAllEdits();
        UpdateEditorButtons();
        FileLabelUpdate();
        if (UseExternalEditor.isSelected()) {
            TextEditor1.get(iTab).setEditable(false);
        }
        log("Loading " + FileName + ": Success.");
        return true;
    }

    private void DelayStateChanged() {//GEN-FIRST:event_DelayStateChanged
        DelayLabel.setText("Delay after answer = " + Integer.toString(Delay.getValue()) + " ms");
        prefs.putInt(Constants.DELAY, Delay.getValue());
        PrefsFlush();
    }//GEN-LAST:event_DelayStateChanged

    private boolean PrefsFlush() {
        boolean success = false;
        try {
            prefs.flush();
            success = true;
        } catch (Exception e) {
            log("ERROR: Can't save some setting.");
            log(e.toString());
        }
        return success;
    }

    private void ButtonFileOpenActionPerformed() {//GEN-FIRST:event_ButtonFileOpenActionPerformed
        MenuItemFileOpen.doClick();
    }//GEN-LAST:event_ButtonFileOpenActionPerformed

    private void ButtonFileSaveActionPerformed() {//GEN-FIRST:event_ButtonFileSaveActionPerformed
        MenuItemFileSave.doClick();
    }//GEN-LAST:event_ButtonFileSaveActionPerformed

    private void ButtonCutActionPerformed() {//GEN-FIRST:event_ButtonCutActionPerformed
        MenuItemEditCut.doClick();
    }//GEN-LAST:event_ButtonCutActionPerformed

    private void ButtonCopyActionPerformed() {//GEN-FIRST:event_ButtonCopyActionPerformed
        MenuItemEditCopy.doClick();
    }//GEN-LAST:event_ButtonCopyActionPerformed

    private void ButtonPasteActionPerformed() {//GEN-FIRST:event_ButtonPasteActionPerformed
        MenuItemEditPaste.doClick();
    }//GEN-LAST:event_ButtonPasteActionPerformed

    private void MenuItemEditorCutActionPerformed() {//GEN-FIRST:event_MenuItemEditorCutActionPerformed
        MenuItemEditCut.doClick();
    }//GEN-LAST:event_MenuItemEditorCutActionPerformed

    private void MenuItemEditorCopyActionPerformed() {//GEN-FIRST:event_MenuItemEditorCopyActionPerformed
        MenuItemEditCopy.doClick();
    }//GEN-LAST:event_MenuItemEditorCopyActionPerformed

    private void MenuItemEditorPasteActionPerformed() {//GEN-FIRST:event_MenuItemEditorPasteActionPerformed
        MenuItemEditPaste.doClick();
    }//GEN-LAST:event_MenuItemEditorPasteActionPerformed

    private void MenuItemLogClearActionPerformed() {//GEN-FIRST:event_MenuItemLogClearActionPerformed
        Log.setText("");
    }//GEN-LAST:event_MenuItemLogClearActionPerformed

    private void CheckSelected() {
        if (TextEditor1.get(iTab).getSelectedText() == null) {
            MenuItemEditorCut.setEnabled(false);
            MenuItemEditCut.setEnabled(false);
            MenuItemEditorCopy.setEnabled(false);
            MenuItemEditCopy.setEnabled(false);
            ButtonCut.setEnabled(false);
            copyButton.setEnabled(false);
            MenuItemEditSendSelected.setEnabled(false);
            MenuItemEditorSendSelected.setEnabled(false);
            ButtonSendSelected.setEnabled(false);
        } else {
            MenuItemEditorCut.setEnabled(true);
            MenuItemEditCut.setEnabled(true);
            MenuItemEditorCopy.setEnabled(true);
            MenuItemEditCopy.setEnabled(true);
            ButtonCut.setEnabled(true);
            copyButton.setEnabled(true);
            MenuItemEditSendSelected.setEnabled(pOpen);
            MenuItemEditorSendSelected.setEnabled(pOpen);
            ButtonSendSelected.setEnabled(pOpen);
        }
        try {
            if (Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null) == null) { // clipboard empty
                MenuItemEditorPaste.setEnabled(false);
                MenuItemEditPaste.setEnabled(false);
                ButtonPaste.setEnabled(false);
            } else {
                MenuItemEditorPaste.setEnabled(true);
                MenuItemEditPaste.setEnabled(true);
                ButtonPaste.setEnabled(true);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    private void UpdateEditorButtons() {
        iTab = FilesTabbedPane.getSelectedIndex();
        // isChanged
        if (isChanged() && !UseExternalEditor.isSelected()) {
            if (isFileNew()) {
                MenuItemFileSave.setEnabled(true);
                ButtonFileSave.setEnabled(true);
                MenuItemFileReload.setEnabled(false);
                ButtonFileReload.setEnabled(false);
            } else {
                MenuItemFileSave.setEnabled(true);
                ButtonFileSave.setEnabled(true);
                MenuItemFileReload.setEnabled(true);
                ButtonFileReload.setEnabled(true);
            }
        } else {
            if (isFileNew()) {
                MenuItemFileSave.setEnabled(true);
                ButtonFileSave.setEnabled(true);
                MenuItemFileReload.setEnabled(false);
                ButtonFileReload.setEnabled(false);
            } else {
                MenuItemFileSave.setEnabled(false);
                ButtonFileSave.setEnabled(false);
                MenuItemFileReload.setEnabled(true);
                ButtonFileReload.setEnabled(true);
            }
        }
        if (isFileNew() && (FilesTabbedPane.getTabCount() == 1)) {
            MenuItemFileClose.setEnabled(false);
            ButtonFileClose.setEnabled(false);
        } else {
            MenuItemFileClose.setEnabled(true);
            ButtonFileClose.setEnabled(true);
        }
        // CanUndo
        try {
            if (TextEditor1.isEmpty()) {
                return;
            }
        } catch (Exception e) {
            return;
        }
        if (TextEditor1.get(iTab).canUndo()) {
            MenuItemEditUndo.setEnabled(true);
            MenuItemEditorUndo.setEnabled(true);
            ButtonUndo.setEnabled(true);
        } else {
            MenuItemEditUndo.setEnabled(false);
            MenuItemEditorUndo.setEnabled(false);
            ButtonUndo.setEnabled(false);
        }
        // CanRedo
        if (TextEditor1.get(iTab).canRedo()) {
            MenuItemEditRedo.setEnabled(true);
            MenuItemEditorRedo.setEnabled(true);
            ButtonRedo.setEnabled(true);
        } else {
            MenuItemEditRedo.setEnabled(false);
            MenuItemEditorRedo.setEnabled(false);
            ButtonRedo.setEnabled(false);
        }
        CheckSelected();
        if (UseExternalEditor.isSelected()) {
            MenuItemFileReload.setEnabled(true);
            ButtonFileReload.setEnabled(true);

            MenuItemFileSave.setEnabled(false);
            ButtonFileSave.setEnabled(false);
            MenuItemEditUndo.setEnabled(false);
            MenuItemEditorUndo.setEnabled(false);
            ButtonUndo.setEnabled(false);
            MenuItemEditRedo.setEnabled(false);
            MenuItemEditorRedo.setEnabled(false);
            ButtonRedo.setEnabled(false);
            MenuItemEditorCut.setEnabled(false);
            MenuItemEditCut.setEnabled(false);
            ButtonCut.setEnabled(false);
            MenuItemEditorPaste.setEnabled(false);
            MenuItemEditPaste.setEnabled(false);
            ButtonPaste.setEnabled(false);
            MenuItemFileNew.setEnabled(false);
            ButtonFileNew.setEnabled(false);
            MenuItemFileSaveAs.setEnabled(false);
        } else {
            MenuItemFileNew.setEnabled(true);
            ButtonFileNew.setEnabled(true);
            MenuItemFileSaveAs.setEnabled(true);
        }
    }

    private void formFocusGained() {//GEN-FIRST:event_formFocusGained
        UpdateEditorButtons();
        UpdateButtons();
    }//GEN-LAST:event_formFocusGained

    private void NodeMCUComponentShown() {//GEN-FIRST:event_NodeMCUComponentShown
        UpdateEditorButtons();
        UpdateButtons();
    }//GEN-LAST:event_NodeMCUComponentShown

    private void ButtonFileReloadActionPerformed() {//GEN-FIRST:event_ButtonFileReloadActionPerformed
        MenuItemFileReload.doClick();
    }//GEN-LAST:event_ButtonFileReloadActionPerformed

    private void MenuItemFileReloadActionPerformed() {//GEN-FIRST:event_MenuItemFileReloadActionPerformed
        ReloadFile();
    }//GEN-LAST:event_MenuItemFileReloadActionPerformed

    private void MenuItemFileCloseActionPerformed() {//GEN-FIRST:event_MenuItemFileCloseActionPerformed
        CloseFile();
    }//GEN-LAST:event_MenuItemFileCloseActionPerformed

    private void ButtonFileCloseActionPerformed() {//GEN-FIRST:event_ButtonFileCloseActionPerformed
        MenuItemFileClose.doClick();
    }//GEN-LAST:event_ButtonFileCloseActionPerformed

    private void FileSaveESPActionPerformed() {//GEN-FIRST:event_FileSaveESPActionPerformed
        if (FileSaveESP.isSelected()) {  // start sending
            if (TextEditor1.get(iTab).getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "File empty.");
                FileSaveESP.setSelected(false);
                return;
            }
            if (FileAutoSaveDisk.isSelected()) {
                if (!SaveFile()) { // first save file
                    return;
                }
            }
            SaveFileESP();
        } else {                     // abort sending or end of file
            StopSend();
        }
    }//GEN-LAST:event_FileSaveESPActionPerformed

    private void MenuItemViewClearTerminalActionPerformed() {//GEN-FIRST:event_MenuItemViewClearTerminalActionPerformed
        MenuItemTerminalClear.doClick();
    }//GEN-LAST:event_MenuItemViewClearTerminalActionPerformed

    private void MenuItemViewClearLogActionPerformed() {//GEN-FIRST:event_MenuItemViewClearLogActionPerformed
        MenuItemLogClear.doClick();
    }//GEN-LAST:event_MenuItemViewClearLogActionPerformed

    private void AlwaysOnTopItemStateChanged() {//GEN-FIRST:event_AlwaysOnTopItemStateChanged
        this.setAlwaysOnTop(AlwaysOnTop.isSelected());
    }//GEN-LAST:event_AlwaysOnTopItemStateChanged

    private void cmdNodeRestartActionPerformed() {//GEN-FIRST:event_cmdNodeRestartActionPerformed
        if (!OptionNodeMCU.isSelected()) {
            return;
        }
        if (portJustOpen) {
            log("ERROR: Communication with MCU not established.");
            return;
        }
        TerminalAdd("Soft restart by user command\r\n");
        btnSend("node.restart()");
        if (pOpen) { // reconnect
            int speed = prefs.getInt(Constants.SERIAL_BAUD, 3);
            final int old_speed = Speed.getSelectedIndex();
            //if (speed == old_speed) { // reconnect not needed
            //return;
            //}
            try {
                nSpeed = Integer.parseInt(Speed.getItemAt(speed).toString());
                //Integer.parseInt((String)Speed.getSelectedItem());
            } catch (Exception e) {
                return;
            }
            log("Try to reconnect with saved baud " + Integer.toString(nSpeed) + "...");
            try {
                if (SetSerialPortParams()) {
                    log("Reconnect: Success. Now we waiting for ESP reboot...");
                    // Now, we can ready to reconnect on old_speed
                    ActionListener taskSleep = new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            if (nSpeed == 9600) {
                                CheckComm();
                            } else {
                                Speed.setSelectedIndex(old_speed);
                            }
                        }
                    };
                    Timer sleep = new Timer(2000, taskSleep);
                    sleep.setRepeats(false);
                    sleep.setInitialDelay(2000);
                    sleep.start();
                }
            } catch (Exception e) {
                log(e.toString());
            }
        }
    }//GEN-LAST:event_cmdNodeRestartActionPerformed

    private void cmdNodeChipIDActionPerformed() {//GEN-FIRST:event_cmdNodeChipIDActionPerformed
        btnSend("print(node.chipid())");
    }//GEN-LAST:event_cmdNodeChipIDActionPerformed

    private void cmdNodeHeapActionPerformed() {//GEN-FIRST:event_cmdNodeHeapActionPerformed
        btnSend("print(node.heap())");
    }//GEN-LAST:event_cmdNodeHeapActionPerformed

    private void cmdNodeSleepActionPerformed() {//GEN-FIRST:event_cmdNodeSleepActionPerformed
        int returnVal = Dialog("This function can only be used in the condition that esp8266 PIN32(RST) and PIN8(XPD_DCDC) are connected together.", JOptionPane.YES_NO_OPTION);
        if (returnVal == JOptionPane.YES_OPTION) {
            btnSend("node.dsleep(10000)");
        }
    }//GEN-LAST:event_cmdNodeSleepActionPerformed

    private void MenuItemEditUndoActionPerformed() {//GEN-FIRST:event_MenuItemEditUndoActionPerformed
        if (TextEditor1.get(iTab).canUndo()) {
            TextEditor1.get(iTab).undoLastAction();
        }
    }//GEN-LAST:event_MenuItemEditUndoActionPerformed

    private void MenuItemEditRedoActionPerformed() {//GEN-FIRST:event_MenuItemEditRedoActionPerformed
        if (TextEditor1.get(iTab).canRedo()) {
            TextEditor1.get(iTab).redoLastAction();
        }
    }//GEN-LAST:event_MenuItemEditRedoActionPerformed

    private void ButtonUndoActionPerformed() {//GEN-FIRST:event_ButtonUndoActionPerformed
        MenuItemEditUndo.doClick();
    }//GEN-LAST:event_ButtonUndoActionPerformed

    private void ButtonRedoActionPerformed() {//GEN-FIRST:event_ButtonRedoActionPerformed
        MenuItemEditRedo.doClick();
    }//GEN-LAST:event_ButtonRedoActionPerformed

    private void MenuItemEditorRedoActionPerformed() {//GEN-FIRST:event_MenuItemEditorRedoActionPerformed
        MenuItemEditRedo.doClick();
    }//GEN-LAST:event_MenuItemEditorRedoActionPerformed

    private void MenuItemEditorUndoActionPerformed() {//GEN-FIRST:event_MenuItemEditorUndoActionPerformed
        MenuItemEditUndo.doClick();
    }//GEN-LAST:event_MenuItemEditorUndoActionPerformed

    private void TextEditorCaretUpdate() {//GEN-FIRST:event_TextEditorCaretUpdate
        UpdateEditorButtons();
    }//GEN-LAST:event_TextEditorCaretUpdate

    private void FilesTabbedPaneStateChanged() {//GEN-FIRST:event_FilesTabbedPaneStateChanged
        FileLabelUpdate();
        //   if (iTab > 0) FilesTabbedPane.setTitleAt(iTab, Integer.toString(iTab));
    }//GEN-LAST:event_FilesTabbedPaneStateChanged

    private void TextEditorCaretPositionChanged() {//GEN-FIRST:event_TextEditorCaretPositionChanged
        UpdateEditorButtons();
    }//GEN-LAST:event_TextEditorCaretPositionChanged

    private void TextEditorActiveLineRangeChanged() {//GEN-FIRST:event_TextEditorActiveLineRangeChanged
        UpdateEditorButtons();
    }//GEN-LAST:event_TextEditorActiveLineRangeChanged

    private void EditorThemeActionPerformed() {//GEN-FIRST:event_EditorThemeActionPerformed
        int n = EditorTheme.getSelectedIndex();
        prefs.putInt(Constants.COLOR_THEME, n);
        PrefsFlush();
        SetTheme(n, true); // for all
    }//GEN-LAST:event_EditorThemeActionPerformed

    private void TextEditorKeyTyped() {//GEN-FIRST:event_TextEditorKeyTyped
        if (UseExternalEditor.isSelected()) {
            return;
        }
        if (!isChanged()) {
            FileChanged.set(iTab, true);
            UpdateEditorButtons();
        }
    }//GEN-LAST:event_TextEditorKeyTyped

    private void FileSendESPActionPerformed() {//GEN-FIRST:event_FileSendESPActionPerformed
        if (FileSendESP.isSelected()) {
            if (TextEditor1.get(iTab).getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "File empty.");
                FileSendESP.setSelected(false);
                return;
            }
            SendToESP(TextEditor1.get(iTab).getText());
        } else {
            StopSend();
        }
    }//GEN-LAST:event_FileSendESPActionPerformed

    private void AnswerDelayStateChanged() {//GEN-FIRST:event_AnswerDelayStateChanged
        AnswerDelayLabel.setText("Answer timout = " + Integer.toString(AnswerDelay.getValue()) + " s");
        prefs.putInt(Constants.TIMEOUT, AnswerDelay.getValue());
        PrefsFlush();
    }//GEN-LAST:event_AnswerDelayStateChanged

    private void cmdListFilesActionPerformed() {//GEN-FIRST:event_cmdListFilesActionPerformed
        ListFiles();
    }//GEN-LAST:event_cmdListFilesActionPerformed

    private void ListFiles() {
        if (portJustOpen) {
            log("ERROR: Communication with MCU not yet established.");
            return;
        }
        //String cmd = "print(\"~~~File \"..\"list START~~~\") for k,v in pairs(file.list()) do l = string.format(\"%-15s\",k) print(l..\" - \"..v..\" bytes\") end l=nil k=nil v=nil print(\"~~~File \"..\"list END~~~\")";
        String cmd = "_dir=function()\n" +
                "     local k,v,l\n" +
                "     print(\"~~~File \"..\"list START~~~\")\n" +
                "     for k,v in pairs(file.list()) do \n" +
                "          l = string.format(\"%-15s\",k) \n" +
                "          print(l..\" : \"..v..\" bytes\") \n" +
                "     end \n" +
                "     print(\"~~~File \"..\"list END~~~\")\n" +
                "end\n" +
                "_dir()\n" +
                "_dir=nil";
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            log(e.toString());
        }
        try {
            serialPort.addEventListener(new PortFilesReader(), Constants.PORT_MASK);
            log("FileManager: Add EventListener: Success.");
        } catch (SerialPortException e) {
            log("FileManager: Add EventListener Error. Canceled.");
            return;
        }
        ClearFileManager();
        rx_data = "";
        rcvBuf = "";
        sendBuf = cmdPrep(cmd);
        log("FileManager: Starting...");
        SendLock();
        int delay = 10;
        j0();
        taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (j < sendBuf.size()) {
                    LocalEcho = false;
                    send(addCR(sendBuf.get(j)), false);
                }
            }
        };
        timer = new Timer(delay, taskPerformer);
        timer.setRepeats(false);
        timer.setInitialDelay(delay);
        WatchDog();
        timer.start();
    }

    private void ClearFileManager() {
        if (!MenuItemViewFileManager.isSelected()) {
            return;
        }
        FileManagerPane.removeAll();
        FileManagerPane.add(FileFormat);
        FileManagerPane.add(FileSystemInfo);
        FileManagerPane.add(FileListReload);
        FileManagerPane.add(FileRenamePanel);
        FileRenamePanel.setVisible(false);
        FileRenamePanel.setEnabled(false);
        FileManagerPane.repaint();
        FileAsButton = new ArrayList<JButton>();
    }

    private void FileDownload(String param) {
        if (portJustOpen) {
            log("Downloader: Communication with MCU not yet established.");
            return;
        }
        // param  init.luaSize:123
        DownloadedFileName = param.split("Size:")[0];
        int size = Integer.parseInt(param.split("Size:")[1]);
        packets = size / 1024;
        if (size % 1024 > 0) packets++;
        sendBuf = new ArrayList<String>();
        rcvPackets = new ArrayList<String>();
        PacketsData = new ArrayList<String>();
        PacketsSize = new ArrayList<Integer>();
        PacketsNum = new ArrayList<Integer>();
        rcvFile = "";
        PacketsByte = new byte[0];
        rx_byte = new byte[0];
        PacketsCRC = new ArrayList<Integer>();
        String cmd = "_dl=function() " +
                "  file.open(\"" + DownloadedFileName + "\", \"r\")\n" +
                "  local buf " +
                "  local i=0 " +
                "  local checksum\n" +
                "  repeat " +
                "     buf = file.read(1024) " +
                "     if buf ~= nil then " +
                "          i = i + 1 " +
                "          checksum = 0 " +
                "          for j=1, string.len(buf) do\n" +
                "               checksum = checksum + (buf:byte(j)*20)%19 " +
                "          end " +
                "          buf='~~~'..'DATA-START~~~'..buf..'~~~'..'DATA-LENGTH~~~'..string.len(buf)..'~~~'..'DATA-N~~~'..i..'~~~'..'DATA-CRC~~~'..checksum..'~~~'..'DATA-END~~~'\n" +
                "          uart.write(0,buf) " +
                "     end " +
                "     tmr.wdclr() " +
                "  until(buf == nil) " +
                "  file.close()\n" +
                "  buf='~~~'..'DATA-TOTAL-START~~~'..i..'~~~'..'DATA-TOTAL-END~~~'\n" +
                "  uart.write(0,buf) " +
                "end " +
                "_dl() " +
                "_dl=nil\n";
        s = cmd.split("\r?\n");
        Collections.addAll(sendBuf, s);
        log("Downloader: Starting...");
        startTime = System.currentTimeMillis();
        SendLock();
        rx_data = "";
        rcvBuf = "";
        rx_byte = new byte[0];
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            log(e.toString());
        }
        try {
            serialPort.addEventListener(new PortFileDownloader(), Constants.PORT_MASK);
            log("Downloader: Add EventListener: Success.");
        } catch (SerialPortException e) {
            log("Downloader: Add EventListener Error. Canceled.");
            return;
        }
        int delay = 10;
        j0();
        taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (j < sendBuf.size()) {
                    send(addCR(sendBuf.get(j)), false);
                }
            }
        };
        timer = new Timer(delay, taskPerformer);
        timer.setRepeats(false);
        log("Downloader: Start");
        TerminalAdd("\r\nDownload file \"" + DownloadedFileName + "\"...");
        timer.setInitialDelay(delay);
        WatchDog();
        timer.start();
    }

    private void FileDownloadFinisher(boolean success) {
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            log(e.toString());
        }
        try {
            serialPort.addEventListener(new PortReader(), Constants.PORT_MASK);
        } catch (SerialPortException e) {
            log("Downloader: Can't Add OldEventListener.");
        }
        //SendUnLock();
        StopSend();
        if (success) {
            TerminalAdd("Success.\r\n");
            if (DownloadCommand.startsWith("EDIT")) {
                FileNew(rcvFile);
            } else if (DownloadCommand.startsWith("DOWNLOAD")) {
                SaveDownloadedFile();
            }
        } else {
            TerminalAdd("FAIL.\r\n");
        }
    }

    private byte[] concatArray(byte[] a, byte[] b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        byte[] r = new byte[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    private byte[] copyPartArray(byte[] a, int start, int len) {
        if (a == null)
            return null;
        if (start > a.length)
            return null;
        byte[] r = new byte[len];
        try {
            System.arraycopy(a, start, r, 0, len);
        } catch (Exception e) {
            log(e.toString());
            log("copyPartArray exception");
            log("size a=" + Integer.toString(a.length));
            log("start =" + Integer.toString(start));
            log("len   =" + Integer.toString(len));
        }
        return r;
    }

    private int FindPacketID(int nPacket) {
        int i, j, n, ret = -1;
        boolean success;
        String s = "~~~DATA-START~~~";
        i = 0;
        n = 0;
        while (i < rx_byte.length - s.length()) {
            success = true;
            for (j = 0; j < s.length(); j++) {
                if (!(rx_byte[i + j] == s.charAt(j))) {
                    success = false;
                    break;
                }
            }
            if (success) {
                n++;
                //log("Downloader: n =" + Integer.toString(n));
                //log("Downloader: nPacket =" + Integer.toString(nPacket));
            }
            if (success && (n == nPacket)) {
                ret = i + s.length();
                break;
            }
            i++;
        }
        //log("Downloader: FindPacketID=" + Integer.toString(ret));
        return ret;
    }

    private int CRC(byte[] s) {
        int cs = 0;
        int x;
        try {
            for (byte value : s) {
                x = value & 0xFF;
                //log( Integer.toHexString(x) );
                cs = cs + (x * 20) % 19;
            }
            //log("\r\nCRC size= " + Integer.toString(s.length)+ ", CRC="+Integer.toString(cs));
        } catch (Exception e) {
            log(e.toString());
            log(Arrays.toString(e.getStackTrace()));
            log("size=" + Integer.toString(s.length));
        }
        return cs;
    }

    private void HexDump(String FileName) {
        String cmd = "_dump=function()\n" +
                "  local buf\n" +
                "  local j=0\n" +
                "  if file.open(\"" + FileName + "\", \"r\") then\n" +
                "  print('--HexDump start')\n" +
                "  repeat\n" +
                "     buf=file.read(1024)\n" +
                "     if buf~=nil then\n" +
                "     local n \n" +
                "     if #buf==1024 then\n" +
                "        n=(#buf/16)*16\n" +
                "     else\n" +
                "        n=(#buf/16+1)*16\n" +
                "     end\n" +
                "     for i=1,n do\n" +
                "         j=j+1\n" +
                "         if (i-1)%16==0 then\n" +
                "            uart.write(0,string.format('%08X  ',j-1)) \n" +
                "         end\n" +
                "         uart.write(0,i>#buf and'   'or string.format('%02X ',buf:byte(i)))\n" +
                "         if i%8==0 then uart.write(0,' ')end\n" +
                "         if i%16==0 then uart.write(0,buf:sub(i-16+1, i):gsub('%c','.'),'\\n')end\n" +
                "         if i%128==0 then tmr.wdclr()end\n" +
                "     end\n" +
                "     end\n" +
                "  until(buf==nil)\n" +
                "  file.close()\n" +
                "  print(\"\\r--HexDump done.\")\n" +
                "  else\n" +
                "  print(\"\\r--HexDump error: can't open file\")\n" +
                "  end\n" +
                "end\n" +
                "_dump()\n" +
                "_dump=nil\n";
        LocalEcho = false;
        SendToESP(cmdPrep(cmd));
    }

    private ArrayList<String> cmdPrep(String cmd) {
        String[] str = cmd.split("\n");
        ArrayList<String> s256 = new ArrayList<String>();
        int i = 0;
        s256.add("");
        for (String subs : str) {
            if ((s256.get(i).length() + subs.trim().length()) <= 250) {
                s256.set(i, s256.get(i) + " " + subs.trim());
            } else {
                s256.set(i, s256.get(i) + "\r");
                s256.add(subs);
                i++;
            }
        }
        return s256;
    }

    private void UpdateLedCTS() {
        try {
            if (serialPort.isCTS()) {
                PortCTS.setIcon(LED_GREEN);
            } else {
                PortCTS.setIcon(LED_GREY);
            }
        } catch (Exception e) {
            log(e.toString());
        }
    }

    private void TerminalAdd(String s) {
        Document doc = Terminal.getDocument();
        if (doc.getLength() > TerminalMax) {
            try {
                doc.remove(0, 1024);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
        try {
            doc.insertString(doc.getLength(), s, null);
        } catch (Exception e) {
            log(e.toString());
        }
        if (AutoScroll.isSelected()) {
            try {
                Terminal.setCaretPosition(doc.getLength());
            } catch (Exception e) {
                log(e.toString());
            }
        }
    }

    private void ButtonSendSelectedActionPerformed() {//GEN-FIRST:event_ButtonSendSelectedActionPerformed
        MenuItemEditSendSelected.doClick();
    }//GEN-LAST:event_ButtonSendSelectedActionPerformed

    private void MenuItemEditorSendSelectedActionPerformed() {//GEN-FIRST:event_MenuItemEditorSendSelectedActionPerformed
        MenuItemEditSendSelected.doClick();
    }//GEN-LAST:event_MenuItemEditorSendSelectedActionPerformed

    private void MenuItemEditSendSelectedActionPerformed() {//GEN-FIRST:event_MenuItemEditSendSelectedActionPerformed
        int l;
        if ((LeftTab.getSelectedIndex() == 0) && (TextTab.getSelectedIndex() == 0)) { // NodeMCU and Scripts
            try {
                l = TextEditor1.get(iTab).getSelectedText().length();
            } catch (Exception e) {
                log("Can't send: nothing selected.");
                return;
            }
            if (l > 0) SendToESP(TextEditor1.get(iTab).getSelectedText());
        } else if ((LeftTab.getSelectedIndex() == 0) && (TextTab.getSelectedIndex() == 0)) { // NodeMCU and Snippets
            try {
                l = SnippetText.getSelectedText().length();
            } catch (Exception e) {
                log("Can't send: nothing selected.");
                return;
            }
            if (l > 0) SendToESP(SnippetText.getSelectedText());
        }
    }

    private void MenuItemFileRemoveESPActionPerformed() {
        String ft = iFile.get(iTab).getName();
        if (ft.length() == 0) {
            log("FileRemoveESP: FAIL. Can't remove file from ESP without name.");
            JOptionPane.showMessageDialog(null, "Can't remove file from ESP without name.");
        }
        FileRemoveESP(ft);
    }

    private void FileRemoveESP(String FileName) {
        btnSend("file.remove(\"" + FileName + "\")");
        try {
            Thread.sleep(200L);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        FileListReload.doClick();
    }

    private void MenuItemFileSaveESPActionPerformed() {//GEN-FIRST:event_MenuItemFileSaveESPActionPerformed
        if (!FileSaveESP.isSelected()) {
            FileSaveESP.doClick();
        }
    }

    private void cmdTimerStopActionPerformed() {//GEN-FIRST:event_cmdTimerStopActionPerformed
        String cmd = "tmr.stop(" + Integer.toString(TimerNumber.getSelectedIndex()) + ")";
        btnSend(cmd);
    }//GEN-LAST:event_cmdTimerStopActionPerformed

    private void NodeResetActionPerformed() {//GEN-FIRST:event_NodeResetActionPerformed
        cmdNodeRestart.doClick();
    }//GEN-LAST:event_NodeResetActionPerformed

    private void FileDoActionPerformed() {//GEN-FIRST:event_FileDoActionPerformed
        String cmd = "dofile('" + iFile.get(iTab).getName() + "')";
        btnSend(cmd);
    }//GEN-LAST:event_FileDoActionPerformed

    private void MenuItemLogCloseActionPerformed() {//GEN-FIRST:event_MenuItemLogCloseActionPerformed
        MenuItemViewLog.doClick();
    }//GEN-LAST:event_MenuItemLogCloseActionPerformed

    private void MenuItemTerminalResetActionPerformed() {//GEN-FIRST:event_MenuItemTerminalResetActionPerformed
        MenuItemESPReset.doClick();
    }//GEN-LAST:event_MenuItemTerminalResetActionPerformed

    private void DonateActionPerformed() {//GEN-FIRST:event_DonateActionPerformed
        goLink(donate_uri);
    }//GEN-LAST:event_DonateActionPerformed

    private void MenuItemHelpAboutActionPerformed() {//GEN-FIRST:event_MenuItemHelpAboutActionPerformed
        aboutDialog.setLocationRelativeTo(null);
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_MenuItemHelpAboutActionPerformed

    private void AboutFocusLost() {//GEN-FIRST:event_AboutFocusLost
        aboutDialog.dispose();
    }//GEN-LAST:event_AboutFocusLost

    private void HomePageActionPerformed() {//GEN-FIRST:event_HomePageActionPerformed
        goLink(homepage_uri);
    }//GEN-LAST:event_HomePageActionPerformed

    private void LeftTabStateChanged() {//GEN-FIRST:event_LeftTabStateChanged
        if (LeftTab.getSelectedIndex() == 0) {  // NodeMCU & Python
            CommandsSetNodeMCU();
        } else if (LeftTab.getSelectedIndex() == 1) {  // AT
            CommandsSetAT();
        }
    }//GEN-LAST:event_LeftTabStateChanged

    private void LoadSnippets() {
        if (OptionNodeMCU.isSelected()) {
            SnippetText.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
            log("Snippets (LUA): loading...");
            for (int i = 0; i <= 15; i++) {
                String n = Integer.toString(i).trim();
                if (prefs.get("Snippet" + n + "name", null) == null) {
                    prefs.put("Snippet" + n + "name", "Snippet" + n);
                    prefs.put("Snippet" + n, "");
                    PrefsFlush();
                }
                if (i == 0) {
                    ButtonSnippet0.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 1) {
                    ButtonSnippet1.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 2) {
                    ButtonSnippet2.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 3) {
                    ButtonSnippet3.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 4) {
                    ButtonSnippet4.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 5) {
                    ButtonSnippet5.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 6) {
                    ButtonSnippet6.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 7) {
                    ButtonSnippet7.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 8) {
                    ButtonSnippet8.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 9) {
                    ButtonSnippet9.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 10) {
                    ButtonSnippet10.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 11) {
                    ButtonSnippet11.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 12) {
                    ButtonSnippet12.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 13) {
                    ButtonSnippet13.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else if (i == 14) {
                    ButtonSnippet14.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                } else {
                    ButtonSnippet15.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
                }
                Snippets[i] = prefs.get("Snippet" + n, "");
            }
            SetSnippetEditButtonsTooltip();
            log("Snippets (LUA) load: Success.");
        } else {
            SnippetText.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
            // python snippets
            log("Snippets (Python): loading...");
            log("Snippets (Python) load: Success.");
        }
    }

    private void SnippetEdit0ActionPerformed() {//GEN-FIRST:event_SnippetEdit0ActionPerformed
        SnippetEdit(0);
    }//GEN-LAST:event_SnippetEdit0ActionPerformed

    private void SnippetEdit(int n) {
        if (SnippetText.canUndo() || SnippetText.canRedo()) {
            this.setAlwaysOnTop(false);
            int returnVal = JOptionPane.showConfirmDialog(null, "Discard any changes and load Snippet" + Integer.toString(n), "Attention", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            this.setAlwaysOnTop(AlwaysOnTop.isSelected());
            if (returnVal != JOptionPane.YES_OPTION) {
                return;
            }
        }
        iSnippets = n;
        SnippetName.setEnabled(true);
        SnippetSave.setEnabled(true);
        SnippetRun.setEnabled(true);
        SnippetCancelEdit.setEnabled(true);
        SnippetText.setEnabled(true);
        SnippetScrollPane.setEnabled(true);
        SnippetText.setEditable(true);
        SnippetText.setText(Snippets[iSnippets]);
        SnippetText.setBackground(themeTextBackground);
        SnippetText.discardAllEdits();
        SnippetName.setText(prefs.get("Snippet" + n + "name", "Snippet" + n));
    }

    private void SnippetSaveActionPerformed() {//GEN-FIRST:event_SnippetSaveActionPerformed
        SnippetName.setEnabled(false);
        SnippetSave.setEnabled(false);
        SnippetRun.setEnabled(true);
        SnippetCancelEdit.setEnabled(true);
        SnippetText.setEnabled(false);
        SnippetScrollPane.setEnabled(false);
        SnippetText.setEditable(false);
        SnippetText.setBackground(SnippetTopPane.getBackground());
        if (iSnippets == 0) {
            ButtonSnippet0.setText(SnippetName.getText());
        } else if (iSnippets == 1) {
            ButtonSnippet1.setText(SnippetName.getText());
        } else if (iSnippets == 2) {
            ButtonSnippet2.setText(SnippetName.getText());
        } else if (iSnippets == 3) {
            ButtonSnippet3.setText(SnippetName.getText());
        } else if (iSnippets == 4) {
            ButtonSnippet4.setText(SnippetName.getText());
        } else if (iSnippets == 5) {
            ButtonSnippet5.setText(SnippetName.getText());
        } else if (iSnippets == 6) {
            ButtonSnippet6.setText(SnippetName.getText());
        } else if (iSnippets == 7) {
            ButtonSnippet7.setText(SnippetName.getText());
        } else if (iSnippets == 8) {
            ButtonSnippet8.setText(SnippetName.getText());
        } else if (iSnippets == 9) {
            ButtonSnippet9.setText(SnippetName.getText());
        } else if (iSnippets == 10) {
            ButtonSnippet10.setText(SnippetName.getText());
        } else if (iSnippets == 11) {
            ButtonSnippet11.setText(SnippetName.getText());
        } else if (iSnippets == 12) {
            ButtonSnippet12.setText(SnippetName.getText());
        } else if (iSnippets == 13) {
            ButtonSnippet13.setText(SnippetName.getText());
        } else if (iSnippets == 14) {
            ButtonSnippet14.setText(SnippetName.getText());
        } else {
            ButtonSnippet15.setText(SnippetName.getText());
            iSnippets = 15;
        }
        SetSnippetEditButtonsTooltip();
        Snippets[iSnippets] = SnippetText.getText();
        prefs.put("Snippet" + Integer.toString(iSnippets) + "name", SnippetName.getText());
        prefs.put("Snippet" + Integer.toString(iSnippets), Snippets[iSnippets]);
        if (PrefsFlush()) {
            log("Snippet" + Integer.toString(iSnippets) + " saved: Success.");
        }
        SnippetText.discardAllEdits();
    }//GEN-LAST:event_SnippetSaveActionPerformed

    private void SnippetEdit1ActionPerformed() {//GEN-FIRST:event_SnippetEdit1ActionPerformed
        SnippetEdit(1);
    }//GEN-LAST:event_SnippetEdit1ActionPerformed

    private void SnippetEdit2ActionPerformed() {//GEN-FIRST:event_SnippetEdit2ActionPerformed
        SnippetEdit(2);
    }//GEN-LAST:event_SnippetEdit2ActionPerformed

    private void SnippetEdit3ActionPerformed() {//GEN-FIRST:event_SnippetEdit3ActionPerformed
        SnippetEdit(3);
    }//GEN-LAST:event_SnippetEdit3ActionPerformed

    private void SnippetEdit4ActionPerformed() {//GEN-FIRST:event_SnippetEdit4ActionPerformed
        SnippetEdit(4);
    }//GEN-LAST:event_SnippetEdit4ActionPerformed

    private void SnippetEdit5ActionPerformed() {//GEN-FIRST:event_SnippetEdit5ActionPerformed
        SnippetEdit(5);
    }//GEN-LAST:event_SnippetEdit5ActionPerformed

    private void SnippetEdit6ActionPerformed() {//GEN-FIRST:event_SnippetEdit6ActionPerformed
        SnippetEdit(6);
    }//GEN-LAST:event_SnippetEdit6ActionPerformed

    private void SnippetEdit7ActionPerformed() {//GEN-FIRST:event_SnippetEdit7ActionPerformed
        SnippetEdit(7);
    }//GEN-LAST:event_SnippetEdit7ActionPerformed
    // End of variables declaration//GEN-END:variables

    private void SnippetEdit8ActionPerformed() {//GEN-FIRST:event_SnippetEdit8ActionPerformed
        SnippetEdit(8);
    }//GEN-LAST:event_SnippetEdit8ActionPerformed

    private void SnippetEdit9ActionPerformed() {//GEN-FIRST:event_SnippetEdit9ActionPerformed
        SnippetEdit(9);
    }//GEN-LAST:event_SnippetEdit9ActionPerformed

    private void SnippetEdit10ActionPerformed() {//GEN-FIRST:event_SnippetEdit10ActionPerformed
        SnippetEdit(10);
    }//GEN-LAST:event_SnippetEdit10ActionPerformed

    private void SnippetEdit11ActionPerformed() {//GEN-FIRST:event_SnippetEdit11ActionPerformed
        SnippetEdit(11);
    }//GEN-LAST:event_SnippetEdit11ActionPerformed

    private void SnippetEdit12ActionPerformed() {//GEN-FIRST:event_SnippetEdit12ActionPerformed
        SnippetEdit(12);
    }//GEN-LAST:event_SnippetEdit12ActionPerformed

    private void SnippetEdit13ActionPerformed() {//GEN-FIRST:event_SnippetEdit13ActionPerformed
        SnippetEdit(13);
    }//GEN-LAST:event_SnippetEdit13ActionPerformed

    private void SnippetEdit14ActionPerformed() {//GEN-FIRST:event_SnippetEdit14ActionPerformed
        SnippetEdit(14);
    }//GEN-LAST:event_SnippetEdit14ActionPerformed

    private void SnippetEdit15ActionPerformed() {//GEN-FIRST:event_SnippetEdit15ActionPerformed
        SnippetEdit(15);
    }//GEN-LAST:event_SnippetEdit15ActionPerformed

    private void ButtonSnippet0ActionPerformed() {//GEN-FIRST:event_ButtonSnippet0ActionPerformed
        DoSnippet(0);
    }//GEN-LAST:event_ButtonSnippet0ActionPerformed

    private void SnippetRunActionPerformed() {//GEN-FIRST:event_SnippetRunActionPerformed
        SendToESP(SnippetText.getText());
    }//GEN-LAST:event_SnippetRunActionPerformed

    private void ButtonSnippet1ActionPerformed() {//GEN-FIRST:event_ButtonSnippet1ActionPerformed
        DoSnippet(1);
    }//GEN-LAST:event_ButtonSnippet1ActionPerformed

    private void ButtonSnippet2ActionPerformed() {//GEN-FIRST:event_ButtonSnippet2ActionPerformed
        DoSnippet(2);
    }//GEN-LAST:event_ButtonSnippet2ActionPerformed

    private void ButtonSnippet3ActionPerformed() {//GEN-FIRST:event_ButtonSnippet3ActionPerformed
        DoSnippet(3);
    }//GEN-LAST:event_ButtonSnippet3ActionPerformed

    private void ButtonSnippet4ActionPerformed() {//GEN-FIRST:event_ButtonSnippet4ActionPerformed
        DoSnippet(4);
    }//GEN-LAST:event_ButtonSnippet4ActionPerformed

    private void ButtonSnippet5ActionPerformed() {//GEN-FIRST:event_ButtonSnippet5ActionPerformed
        DoSnippet(5);
    }//GEN-LAST:event_ButtonSnippet5ActionPerformed

    private void ButtonSnippet6ActionPerformed() {//GEN-FIRST:event_ButtonSnippet6ActionPerformed
        DoSnippet(6);
    }//GEN-LAST:event_ButtonSnippet6ActionPerformed

    private void ButtonSnippet7ActionPerformed() {//GEN-FIRST:event_ButtonSnippet7ActionPerformed
        DoSnippet(7);
    }//GEN-LAST:event_ButtonSnippet7ActionPerformed

    private void ButtonSnippet8ActionPerformed() {//GEN-FIRST:event_ButtonSnippet8ActionPerformed
        DoSnippet(8);
    }//GEN-LAST:event_ButtonSnippet8ActionPerformed

    private void ButtonSnippet9ActionPerformed() {//GEN-FIRST:event_ButtonSnippet9ActionPerformed
        DoSnippet(9);
    }//GEN-LAST:event_ButtonSnippet9ActionPerformed

    private void ButtonSnippet10ActionPerformed() {//GEN-FIRST:event_ButtonSnippet10ActionPerformed
        DoSnippet(10);
    }//GEN-LAST:event_ButtonSnippet10ActionPerformed

    private void ButtonSnippet11ActionPerformed() {//GEN-FIRST:event_ButtonSnippet11ActionPerformed
        DoSnippet(11);
    }//GEN-LAST:event_ButtonSnippet11ActionPerformed

    private void ButtonSnippet12ActionPerformed() {//GEN-FIRST:event_ButtonSnippet12ActionPerformed
        DoSnippet(12);
    }//GEN-LAST:event_ButtonSnippet12ActionPerformed

    private void ButtonSnippet13ActionPerformed() {//GEN-FIRST:event_ButtonSnippet13ActionPerformed
        DoSnippet(13);
    }//GEN-LAST:event_ButtonSnippet13ActionPerformed

    private void ButtonSnippet14ActionPerformed() {//GEN-FIRST:event_ButtonSnippet14ActionPerformed
        DoSnippet(14);
    }//GEN-LAST:event_ButtonSnippet14ActionPerformed

    private void ButtonSnippet15ActionPerformed() {//GEN-FIRST:event_ButtonSnippet15ActionPerformed
        DoSnippet(15);
    }//GEN-LAST:event_ButtonSnippet15ActionPerformed

    private void OptionNodeMCUItemStateChanged() {//GEN-FIRST:event_OptionNodeMCUItemStateChanged
        if (OptionNodeMCU.isSelected()) {
            prefs.put(Constants.FIRMWARE, "NodeMCU");
            PrefsFlush();
            chooser.setFileFilter(FILTER_LUA);
        }
    }//GEN-LAST:event_OptionNodeMCUItemStateChanged

    private void OptionMicroPythonItemStateChanged() {//GEN-FIRST:event_OptionMicroPythonItemStateChanged
        if (OptionMicroPython.isSelected()) {
            prefs.put(Constants.FIRMWARE, "MicroPython");
            PrefsFlush();
            chooser.setFileFilter(FILTER_PYTHON);
        }
    }//GEN-LAST:event_OptionMicroPythonItemStateChanged

    private void FileAutoSaveDiskItemStateChanged() {//GEN-FIRST:event_FileAutoSaveDiskItemStateChanged
        prefs.putBoolean(Constants.FILE_AUTO_SAVE_DISK, FileAutoSaveDisk.isSelected());
        PrefsFlush();
    }//GEN-LAST:event_FileAutoSaveDiskItemStateChanged

    private void FileAutoSaveESPItemStateChanged() {//GEN-FIRST:event_FileAutoSaveESPItemStateChanged
        prefs.putBoolean(Constants.FILE_AUTO_SAVE_ESP, FileAutoSaveESP.isSelected());
        PrefsFlush();
    }//GEN-LAST:event_FileAutoSaveESPItemStateChanged

    private void FileAutoRunItemStateChanged() {//GEN-FIRST:event_FileAutoRunItemStateChanged
        prefs.putBoolean(Constants.FILE_AUTO_RUN, FileAutoRun.isSelected());
        PrefsFlush();
    }//GEN-LAST:event_FileAutoRunItemStateChanged

    private void LineDelayStateChanged() {//GEN-FIRST:event_LineDelayStateChanged
        LineDelayLabel.setText("Line delay for \"Dumb Mode\" = " + Integer.toString(LineDelay.getValue()) + " ms");
        prefs.putInt(Constants.LINE_DELAY, LineDelay.getValue());
        PrefsFlush();
    }//GEN-LAST:event_LineDelayStateChanged

    private void DumbModeItemStateChanged() {//GEN-FIRST:event_DumbModeItemStateChanged
        if (DumbMode.isSelected()) {
            DelayLabel.setEnabled(false);
            Delay.setEnabled(false);
            AnswerDelayLabel.setEnabled(false);
            AnswerDelay.setEnabled(false);
            LineDelayLabel.setEnabled(true);
            LineDelay.setEnabled(true);
            TurboMode.setSelected(false);
            TurboMode.setEnabled(false);
        } else {
            DelayLabel.setEnabled(true);
            Delay.setEnabled(true);
            AnswerDelayLabel.setEnabled(true);
            AnswerDelay.setEnabled(true);
            LineDelayLabel.setEnabled(false);
            LineDelay.setEnabled(false);
            TurboMode.setEnabled(true);
        }
        prefs.putBoolean(Constants.DUMB_MODE, DumbMode.isSelected());
        PrefsFlush();
    }//GEN-LAST:event_DumbModeItemStateChanged

    private void ButtonSendLineActionPerformed() {//GEN-FIRST:event_ButtonSendLineActionPerformed
        MenuItemEditSendLine.doClick();
    }//GEN-LAST:event_ButtonSendLineActionPerformed

    private void MenuItemEditSendLineActionPerformed() {//GEN-FIRST:event_MenuItemEditSendLineActionPerformed
        int nLine;
        if ((LeftTab.getSelectedIndex() == 0) && (TextTab.getSelectedIndex() == 0)) { // NodeMCU and Scripts
            nLine = TextEditor1.get(iTab).getCaretLineNumber();
            String cmd = TextEditor1.get(iTab).getText().split("\r?\n")[nLine];
            btnSend(cmd);
        } else if ((LeftTab.getSelectedIndex() == 0) && (TextTab.getSelectedIndex() == 2)) { // NodeMCU and Snippets
            nLine = SnippetText.getCaretLineNumber();
            String cmd = SnippetText.getText().split("\r?\n")[nLine];
            btnSend(cmd);
        }
    }//GEN-LAST:event_MenuItemEditSendLineActionPerformed

    private void MenuItemFileSendESPActionPerformed() {//GEN-FIRST:event_MenuItemFileSendESPActionPerformed
        FileSendESP.doClick();
    }//GEN-LAST:event_MenuItemFileSendESPActionPerformed

    private void MenuItemEditorSendLineActionPerformed() {//GEN-FIRST:event_MenuItemEditorSendLineActionPerformed
        MenuItemEditSendLine.doClick();
    }//GEN-LAST:event_MenuItemEditorSendLineActionPerformed

    private void ButtonFileSaveAllActionPerformed() {//GEN-FIRST:event_ButtonFileSaveAllActionPerformed
        MenuItemFileSaveAll.doClick();
    }//GEN-LAST:event_ButtonFileSaveAllActionPerformed

    private void MenuItemFileDoActionPerformed() {//GEN-FIRST:event_MenuItemFileDoActionPerformed
        FileDo.doClick();
    }//GEN-LAST:event_MenuItemFileDoActionPerformed

    private void MenuItemTerminalFontDecActionPerformed() {//GEN-FIRST:event_MenuItemTerminalFontDecActionPerformed
        MenuItemViewTermFontDec.doClick();
    }//GEN-LAST:event_MenuItemTerminalFontDecActionPerformed

    private void MenuItemTerminalFontIncActionPerformed() {//GEN-FIRST:event_MenuItemTerminalFontIncActionPerformed
        MenuItemViewTermFontInc.doClick();
    }//GEN-LAST:event_MenuItemTerminalFontIncActionPerformed

    private void MenuItemViewTermFontIncActionPerformed() {//GEN-FIRST:event_MenuItemViewTermFontIncActionPerformed
        int size = Terminal.getFont().getSize();
        if (size < Constants.TERMINAL_FONT_SIZE_MAX) {
            Terminal.setFont(Terminal.getFont().deriveFont(Terminal.getFont().getSize() + 1f));
        } else {
            Terminal.setFont(Terminal.getFont().deriveFont(Constants.TERMINAL_FONT_SIZE_MIN));
        }
        prefs.putFloat(Constants.TERMINAL_FONT_SIZE, Terminal.getFont().getSize());
        PrefsFlush();
    }//GEN-LAST:event_MenuItemViewTermFontIncActionPerformed

    private void MenuItemViewTermFontDecActionPerformed() {//GEN-FIRST:event_MenuItemViewTermFontDecActionPerformed
        int size = Terminal.getFont().getSize();
        if (size > Constants.TERMINAL_FONT_SIZE_MIN) {
            Terminal.setFont(Terminal.getFont().deriveFont(Terminal.getFont().getSize() - 1f));
        } else {
            Terminal.setFont(Terminal.getFont().deriveFont(Constants.TERMINAL_FONT_SIZE_MAX));
        }
        prefs.putFloat(Constants.TERMINAL_FONT_SIZE, Terminal.getFont().getSize());
        PrefsFlush();
    }//GEN-LAST:event_MenuItemViewTermFontDecActionPerformed

    private void MenuItemViewEditorFontIncActionPerformed() {//GEN-FIRST:event_MenuItemViewEditorFontIncActionPerformed
        int size = TextEditor1.get(iTab).getFont().getSize();
        if (size < Constants.EDITOR_FONT_SIZE_MAX) {
            TextEditor1.get(iTab).setFont(TextEditor1.get(iTab).getFont().deriveFont(TextEditor1.get(iTab).getFont().getSize() + 1f));
        } else {
            TextEditor1.get(iTab).setFont(TextEditor1.get(iTab).getFont().deriveFont(Constants.EDITOR_FONT_SIZE_MIN));
        }
        prefs.putFloat(Constants.EDITOR_FONT_SIZE, TextEditor1.get(iTab).getFont().getSize());
        PrefsFlush();
        SetTheme(prefs.getInt(Constants.COLOR_THEME, 0), true); // for all
    }//GEN-LAST:event_MenuItemViewEditorFontIncActionPerformed

    private void MenuItemViewEditorFontDecActionPerformed() {//GEN-FIRST:event_MenuItemViewEditorFontDecActionPerformed
        int size = TextEditor1.get(iTab).getFont().getSize();
        if (size > Constants.EDITOR_FONT_SIZE_MIN) {
            TextEditor1.get(iTab).setFont(TextEditor1.get(iTab).getFont().deriveFont(TextEditor1.get(iTab).getFont().getSize() - 1f));
        } else {
            TextEditor1.get(iTab).setFont(TextEditor1.get(iTab).getFont().deriveFont(Constants.EDITOR_FONT_SIZE_MAX));
        }
        prefs.putFloat(Constants.EDITOR_FONT_SIZE, TextEditor1.get(iTab).getFont().getSize());
        PrefsFlush();
        SetTheme(prefs.getInt(Constants.COLOR_THEME, 0), true); // for all
    }//GEN-LAST:event_MenuItemViewEditorFontDecActionPerformed

    private void MenuItemViewFontDefaultActionPerformed() {//GEN-FIRST:event_MenuItemViewFontDefaultActionPerformed
        prefs.putFloat(Constants.TERMINAL_FONT_SIZE, Constants.TERMINAL_FONT_SIZE_DEFAULT);
        prefs.putFloat(Constants.EDITOR_FONT_SIZE, Constants.EDITOR_FONT_SIZE_DEFAULT);
        prefs.putFloat(Constants.LOG_FONT_SIZE, Constants.LOG_FONT_SIZE_DEFAULT);
        PrefsFlush();
        SetTheme(prefs.getInt(Constants.COLOR_THEME, 0), true); // for all
        Terminal.setFont(Terminal.getFont().deriveFont(Constants.TERMINAL_FONT_SIZE_DEFAULT));
        Log.setFont(Log.getFont().deriveFont(Constants.LOG_FONT_SIZE_DEFAULT));
    }//GEN-LAST:event_MenuItemViewFontDefaultActionPerformed

    private void MenuItemEditorFontIncActionPerformed() {//GEN-FIRST:event_MenuItemEditorFontIncActionPerformed
        MenuItemViewEditorFontInc.doClick();
    }//GEN-LAST:event_MenuItemEditorFontIncActionPerformed

    private void MenuItemEditorFontDecActionPerformed() {//GEN-FIRST:event_MenuItemEditorFontDecActionPerformed
        MenuItemViewEditorFontDec.doClick();
    }//GEN-LAST:event_MenuItemEditorFontDecActionPerformed

    private void MenuItemViewLogFontIncActionPerformed() {//GEN-FIRST:event_MenuItemViewLogFontIncActionPerformed
        int size = Log.getFont().getSize();
        if (size < Constants.LOG_FONT_SIZE_MAX) {
            Log.setFont(Log.getFont().deriveFont(Log.getFont().getSize() + 1f));
        } else {
            Log.setFont(Log.getFont().deriveFont(Constants.LOG_FONT_SIZE_MIN));
        }
        prefs.putFloat(Constants.LOG_FONT_SIZE, Log.getFont().getSize());
        PrefsFlush();
    }//GEN-LAST:event_MenuItemViewLogFontIncActionPerformed

    private void MenuItemViewLogFontDecActionPerformed() {//GEN-FIRST:event_MenuItemViewLogFontDecActionPerformed
        int size = Log.getFont().getSize();
        if (size > Constants.LOG_FONT_SIZE_MIN) {
            Log.setFont(Log.getFont().deriveFont(Log.getFont().getSize() - 1f));
        } else {
            Log.setFont(Log.getFont().deriveFont(Constants.LOG_FONT_SIZE_MAX));
        }
        prefs.putFloat(Constants.LOG_FONT_SIZE, Log.getFont().getSize());
        PrefsFlush();
    }//GEN-LAST:event_MenuItemViewLogFontDecActionPerformed

    private void MenuItemLogFontIncActionPerformed() {//GEN-FIRST:event_MenuItemLogFontIncActionPerformed
        MenuItemViewLogFontInc.doClick();
    }//GEN-LAST:event_MenuItemLogFontIncActionPerformed

    private void MenuItemLogFontDecActionPerformed() {//GEN-FIRST:event_MenuItemLogFontDecActionPerformed
        MenuItemViewLogFontDec.doClick();
    }//GEN-LAST:event_MenuItemLogFontDecActionPerformed

    private void SpeedItemStateChanged() {//GEN-FIRST:event_SpeedItemStateChanged
        nSpeed = Integer.parseInt((String) Speed.getSelectedItem());

    }//GEN-LAST:event_SpeedItemStateChanged

    private void LogMaxSizeFocusLost() {//GEN-FIRST:event_LogMaxSizeFocusLost
        try {
            LogMax = Integer.parseInt(LogMaxSize.getText()) * 1024;
        } catch (Exception e) {
            LogMax = 1024 * 10;
        }
        if (LogMax < 2048) {
            LogMax = 2048;
        } else if (LogMax > 32 * 1024) {
            LogMax = 32 * 1024;
        }
        prefs.putInt(Constants.LOG_MAX_SIZE, LogMax);
        log("Log max size set to " + Integer.toString(LogMax / 1024) + " KB");
    }//GEN-LAST:event_LogMaxSizeFocusLost

    private void TerminalMaxSizeFocusLost() {//GEN-FIRST:event_TerminalMaxSizeFocusLost
        try {
            TerminalMax = Integer.parseInt(TerminalMaxSize.getText()) * 1024;
        } catch (Exception e) {
            TerminalMax = 1024 * 100;
        }
        if (TerminalMax < 2048) {
            TerminalMax = 2048;
        } else if (TerminalMax > 1024 * 1024) {
            TerminalMax = 1024 * 1024;
        }
        prefs.putInt(Constants.TERMINAL_MAX_SIZE, TerminalMax);
        log("Terminal max size set to " + Integer.toString(TerminalMax / 1024) + " KB");
    }//GEN-LAST:event_TerminalMaxSizeFocusLost

    private void SnippetCancelEditActionPerformed() {//GEN-FIRST:event_SnippetCancelEditActionPerformed
        if (SnippetText.canUndo() || SnippetText.canRedo()) {
            this.setAlwaysOnTop(false);
            int returnVal = JOptionPane.showConfirmDialog(null, "Discard any changes and CANCEL edit this snippet without saving?", "Attention", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            this.setAlwaysOnTop(AlwaysOnTop.isSelected());
            if (returnVal != JOptionPane.YES_OPTION) {
                return;
            }
        }
        SnippetName.setEnabled(false);
        SnippetSave.setEnabled(false);
        SnippetRun.setEnabled(false);
        SnippetCancelEdit.setEnabled(false);
        SnippetText.setEnabled(false);
        SnippetScrollPane.setEnabled(false);
        SnippetText.setEditable(false);
        SnippetText.setText("");
        SnippetText.discardAllEdits();
        SnippetName.setText("");
        SnippetText.setBackground(SnippetTopPane.getBackground());
    }//GEN-LAST:event_SnippetCancelEditActionPerformed

    private void TurboModeActionPerformed() {//GEN-FIRST:event_TurboModeActionPerformed
        if (TurboMode.isSelected()) {
            DumbMode.setEnabled(false);
            DumbMode.setSelected(false);
        } else {
            DumbMode.setEnabled(true);
        }
        prefs.putBoolean(Constants.TURBO_MODE, TurboMode.isSelected());
        PrefsFlush();
    }//GEN-LAST:event_TurboModeActionPerformed

    private void MenuItemESPResetActionPerformed() {//GEN-FIRST:event_MenuItemESPResetActionPerformed
        if (LeftTab.getSelectedIndex() == 0) {              // NodeMCU TAB
            cmdNodeRestart.doClick();
        }
    }//GEN-LAST:event_MenuItemESPResetActionPerformed

    private void MenuItemESPFormatActionPerformed() {//GEN-FIRST:event_MenuItemESPFormatActionPerformed
        if (LeftTab.getSelectedIndex() == 0) {              // NodeMCU TAB
            int isFormat = Dialog("Format ESP flash data area and remove ALL files. Are you sure?", JOptionPane.YES_NO_OPTION);
            if (isFormat == JOptionPane.YES_OPTION) {
                btnSend("file.format()");
            }
        }
    }//GEN-LAST:event_MenuItemESPFormatActionPerformed

    private void MenuItemTerminalFormatActionPerformed() {//GEN-FIRST:event_MenuItemTerminalFormatActionPerformed
        MenuItemESPFormat.doClick();
    }//GEN-LAST:event_MenuItemTerminalFormatActionPerformed

    private void MenuItemLinksAPIenActionPerformed() {//GEN-FIRST:event_MenuItemLinksAPIenActionPerformed
        goLink(api_en_uri);
    }//GEN-LAST:event_MenuItemLinksAPIenActionPerformed

    private void MenuItemLinksAPIruActionPerformed() {//GEN-FIRST:event_MenuItemLinksAPIruActionPerformed
        goLink(api_ru_uri);
    }//GEN-LAST:event_MenuItemLinksAPIruActionPerformed

    private void MenuItemLinksChangelogActionPerformed() {//GEN-FIRST:event_MenuItemLinksChangelogActionPerformed
        goLink(changelog_uri);
    }//GEN-LAST:event_MenuItemLinksChangelogActionPerformed

    private void MenuItemLinksDownloadLatestFirmwareActionPerformed() {//GEN-FIRST:event_MenuItemLinksDownloadLatestFirmwareActionPerformed
        goLink(nodemcu_download_latest_uri);
    }//GEN-LAST:event_MenuItemLinksDownloadLatestFirmwareActionPerformed

    private void MenuItemLinksAPIcnActionPerformed() {//GEN-FIRST:event_MenuItemLinksAPIcnActionPerformed
        goLink(api_cn_uri);
    }//GEN-LAST:event_MenuItemLinksAPIcnActionPerformed

    private void CustomPortNameFocusLost() {//GEN-FIRST:event_CustomPortNameFocusLost
        prefs.put(Constants.CUSTOM_PORT_NAME, CustomPortName.getText());
    }//GEN-LAST:event_CustomPortNameFocusLost

    private void FileCompileActionPerformed() {//GEN-FIRST:event_FileCompileActionPerformed
        String cmd = "node.compile('" + iFile.get(iTab).getName() + "')";
        btnSend(cmd);
        try {
            Thread.sleep(500L);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        LocalEcho = false;
        FileListReload.doClick();
    }//GEN-LAST:event_FileCompileActionPerformed

    private void FileCompileDoLCActionPerformed() {//GEN-FIRST:event_FileCompileDoLCActionPerformed
        String fn = iFile.get(iTab).getName();
        String[] part = fn.split(".");
        String cmd = "node.compile('" + fn + "')\r\ndofile(\"" + part[0] + ".lc" + "\")";
        btnSend(cmd);
    }//GEN-LAST:event_FileCompileDoLCActionPerformed

    private void logoMouseClicked() {//GEN-FIRST:event_logoMouseClicked
        MenuItemHelpAbout.doClick();
    }//GEN-LAST:event_logoMouseClicked

    private void RightFilesSplitPanePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_RightFilesSplitPanePropertyChange
        if ("dividerLocation".equals(evt.getPropertyName()) && MenuItemViewFileManager.isSelected()) {
            prefs.putInt(Constants.FM_DIV, RightFilesSplitPane.getDividerLocation());
        }
    }//GEN-LAST:event_RightFilesSplitPanePropertyChange

    private void formComponentResized() {//GEN-FIRST:event_formComponentResized
        isFileManagerShow();
    }//GEN-LAST:event_formComponentResized

    private void FilesUploadActionPerformed() {//GEN-FIRST:event_FilesUploadActionPerformed
        //log(evt.paramString());
        UploadFiles();
    }//GEN-LAST:event_FilesUploadActionPerformed

    private void FileListReloadActionPerformed() {//GEN-FIRST:event_FileListReloadActionPerformed
        ListFiles();
    }//GEN-LAST:event_FileListReloadActionPerformed

    private void FileAsButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileAsButton1ActionPerformed
        String fn = evt.getActionCommand();
        if (fn.endsWith(".lua") || fn.endsWith(".lc")) {
            String cmd = "dofile(\"" + fn + "\")";
            btnSend(cmd);
        } else if (fn.endsWith(".bin") || fn.endsWith(".dat")) {
            HexDump(fn);
        } else {
            ViewFile(fn);
        }
    }//GEN-LAST:event_FileAsButton1ActionPerformed

    private void NodeInfoActionPerformed() {//GEN-FIRST:event_NodeInfoActionPerformed
        btnSend("=node.info()");
    }//GEN-LAST:event_NodeInfoActionPerformed

    private void NodeChipIDActionPerformed() {//GEN-FIRST:event_NodeChipIDActionPerformed
        btnSend("=node.chipid()");
    }//GEN-LAST:event_NodeChipIDActionPerformed

    private void NodeFlashIDActionPerformed() {//GEN-FIRST:event_NodeFlashIDActionPerformed
        btnSend("=node.flashid()");
    }//GEN-LAST:event_NodeFlashIDActionPerformed

    private void NodeHeapActionPerformed() {//GEN-FIRST:event_NodeHeapActionPerformed
        btnSend("=node.heap()");
    }//GEN-LAST:event_NodeHeapActionPerformed

    private void FileFormatActionPerformed() {//GEN-FIRST:event_FileFormatActionPerformed
        MenuItemESPFormat.doClick();
    }//GEN-LAST:event_FileFormatActionPerformed

    private void MenuItemViewSnippetsActionPerformed() {//GEN-FIRST:event_MenuItemViewSnippetsActionPerformed
        prefs.putBoolean(Constants.SHOW_SNIP_RIGHT, MenuItemViewSnippets.isSelected());
        isRightSnippetsShow();
    }//GEN-LAST:event_MenuItemViewSnippetsActionPerformed

    private void MenuItemViewFileManagerActionPerformed() {//GEN-FIRST:event_MenuItemViewFileManagerActionPerformed
        prefs.putBoolean(Constants.SHOW_FM_RIGHT, MenuItemViewFileManager.isSelected());
        isFileManagerShow();
    }//GEN-LAST:event_MenuItemViewFileManagerActionPerformed

    private void MenuItemViewRightExtraActionPerformed() {//GEN-FIRST:event_MenuItemViewRightExtraActionPerformed
        RightExtraButtons.setVisible(MenuItemViewRightExtra.isSelected());
        prefs.putBoolean(Constants.SHOW_EXTRA_RIGHT, MenuItemViewRightExtra.isSelected());
    }//GEN-LAST:event_MenuItemViewRightExtraActionPerformed

    private void MenuItemViewToolbarActionPerformed() {//GEN-FIRST:event_MenuItemViewToolbarActionPerformed
        prefs.putBoolean(Constants.SHOW_TOOLBAR, MenuItemViewToolbar.isSelected());
        isToolbarShow();
    }//GEN-LAST:event_MenuItemViewToolbarActionPerformed

    private void MenuItemViewLeftExtraActionPerformed() {//GEN-FIRST:event_MenuItemViewLeftExtraActionPerformed
        prefs.putBoolean(Constants.SHOW_EXTRA_LEFT, MenuItemViewLeftExtra.isSelected());
        isLeftExtraShow();
    }//GEN-LAST:event_MenuItemViewLeftExtraActionPerformed

    private void UseCustomPortNameActionPerformed() {//GEN-FIRST:event_UseCustomPortNameActionPerformed
        Port.setEnabled(!UseCustomPortName.isSelected());
        Port.setVisible(!UseCustomPortName.isSelected());
        CustomPortName.setEnabled(UseCustomPortName.isSelected());
        prefs.putBoolean(Constants.USE_CUSTOM_PORT, UseCustomPortName.isSelected());
    }//GEN-LAST:event_UseCustomPortNameActionPerformed

    private void RightSplitPanePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_RightSplitPanePropertyChange
        if ("dividerLocation".equals(evt.getPropertyName()) && MenuItemViewLog.isSelected()) {
            prefs.putInt(Constants.LOG_DIV, RightSplitPane.getDividerLocation());
        }
    }//GEN-LAST:event_RightSplitPanePropertyChange

    private void AutoScrollActionPerformed() {//GEN-FIRST:event_AutoScrollActionPerformed
        prefs.putBoolean(Constants.AUTO_SCROLL, AutoScroll.isSelected());
    }//GEN-LAST:event_AutoScrollActionPerformed

    private void PortDTRActionPerformed() {//GEN-FIRST:event_PortDTRActionPerformed
        prefs.putBoolean(Constants.PORT_DTR, PortDTR.isSelected());
        try {
            serialPort.setDTR(PortDTR.isSelected());
            if (PortDTR.isSelected()) {
                log("DTR set to ON");
            } else {
                log("DTR set to OFF");
            }
        } catch (Exception e) {
            PortDTR.setSelected(false);
            log(e.toString());
            log("Can't change DTR state");
        }
        UpdateLED();
    }//GEN-LAST:event_PortDTRActionPerformed

    private void PortRTSActionPerformed() {//GEN-FIRST:event_PortRTSActionPerformed
        prefs.putBoolean(Constants.PORT_RTS, PortRTS.isSelected());
        try {
            serialPort.setRTS(PortRTS.isSelected());
            if (PortRTS.isSelected()) {
                log("RTS set to ON");
            } else {
                log("RTS set to OFF");
            }
        } catch (Exception e) {
            PortRTS.setSelected(false);
            log(e.toString());
            log("Can't change RTS state");
        }
        UpdateLED();
    }//GEN-LAST:event_PortRTSActionPerformed

    private void UseExternalEditorItemStateChanged() {//GEN-FIRST:event_UseExternalEditorItemStateChanged
        prefs.putBoolean(Constants.USE_EXT_EDITOR, UseExternalEditor.isSelected());
        UpdateButtons();
    }//GEN-LAST:event_UseExternalEditorItemStateChanged

    private void MenuItemViewToolbarItemStateChanged() {//GEN-FIRST:event_MenuItemViewToolbarItemStateChanged
        prefs.putBoolean(Constants.SHOW_TOOLBAR, MenuItemViewToolbar.isSelected());
        isToolbarShow();
    }//GEN-LAST:event_MenuItemViewToolbarItemStateChanged

    private void MenuItemViewLeftExtraItemStateChanged() {//GEN-FIRST:event_MenuItemViewLeftExtraItemStateChanged
        prefs.putBoolean(Constants.SHOW_EXTRA_LEFT, MenuItemViewLeftExtra.isSelected());
        isLeftExtraShow();
    }//GEN-LAST:event_MenuItemViewLeftExtraItemStateChanged

    private void MenuItemViewSnippetsItemStateChanged() {//GEN-FIRST:event_MenuItemViewSnippetsItemStateChanged
        prefs.putBoolean(Constants.SHOW_SNIP_RIGHT, MenuItemViewSnippets.isSelected());
        isRightSnippetsShow();
    }//GEN-LAST:event_MenuItemViewSnippetsItemStateChanged

    private void MenuItemViewFileManagerItemStateChanged() {//GEN-FIRST:event_MenuItemViewFileManagerItemStateChanged
        prefs.putBoolean(Constants.SHOW_FM_RIGHT, MenuItemViewFileManager.isSelected());
        isFileManagerShow();
    }//GEN-LAST:event_MenuItemViewFileManagerItemStateChanged

    private void MenuItemViewRightExtraItemStateChanged() {//GEN-FIRST:event_MenuItemViewRightExtraItemStateChanged
        RightExtraButtons.setVisible(MenuItemViewRightExtra.isSelected());
        prefs.putBoolean(Constants.SHOW_EXTRA_RIGHT, MenuItemViewRightExtra.isSelected());
    }//GEN-LAST:event_MenuItemViewRightExtraItemStateChanged

    private void MenuItemViewLogItemStateChanged() {//GEN-FIRST:event_MenuItemViewLogItemStateChanged
        isLogShow();
        prefs.putBoolean(Constants.SHOW_LOG, MenuItemViewLog.isSelected());
    }//GEN-LAST:event_MenuItemViewLogItemStateChanged

    private void FileSystemInfoActionPerformed() {//GEN-FIRST:event_FileSystemInfoActionPerformed
        FileSystemInfo();
    }//GEN-LAST:event_FileSystemInfoActionPerformed

    private void FileRenameActionPerformed() {//GEN-FIRST:event_FileRenameActionPerformed
        btnSend("file.rename(\"" + FileRenameLabel.getText() + "\",\"" + FileRename.getText().trim() + "\")");
        try {
            Thread.sleep(200L);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        FileListReload.doClick();
    }//GEN-LAST:event_FileRenameActionPerformed

    private void MenuItemViewDonateItemStateChanged() {//GEN-FIRST:event_MenuItemViewDonateItemStateChanged
        DonateSmall.setVisible(!MenuItemViewDonate.isSelected());
        prefs.putBoolean(Constants.SHOW_DONATE, MenuItemViewDonate.isSelected());
    }//GEN-LAST:event_MenuItemViewDonateItemStateChanged

    private void DonateSmallActionPerformed() {//GEN-FIRST:event_DonateSmallActionPerformed
        goLink(donate_uri);
    }//GEN-LAST:event_DonateSmallActionPerformed

    private void MenuItemLinksDownloadLatestFlasherActionPerformed() {//GEN-FIRST:event_MenuItemLinksDownloadLatestFlasherActionPerformed
        goLink(flasher_uri);
    }//GEN-LAST:event_MenuItemLinksDownloadLatestFlasherActionPerformed

    private void MenuItemLinksBuyDevBoardActionPerformed() {//GEN-FIRST:event_MenuItemLinksBuyDevBoardActionPerformed
        goLink(buy_nodeMCU);
    }//GEN-LAST:event_MenuItemLinksBuyDevBoardActionPerformed

    private void MenuItemLinksBuyESP8266ActionPerformed() {//GEN-FIRST:event_MenuItemLinksBuyESP8266ActionPerformed
        goLink(buy_esp8266);
    }//GEN-LAST:event_MenuItemLinksBuyESP8266ActionPerformed

    private void MenuItemLinksBuyESD12ActionPerformed() {//GEN-FIRST:event_MenuItemLinksBuyESD12ActionPerformed
        goLink(buy_esd12);
    }//GEN-LAST:event_MenuItemLinksBuyESD12ActionPerformed

    private void MenuItemLinksESPlorerForumEnActionPerformed() {//GEN-FIRST:event_MenuItemLinksESPlorerForumEnActionPerformed
        goLink(esp8266com_uri);
    }//GEN-LAST:event_MenuItemLinksESPlorerForumEnActionPerformed

    private void MenuItemLinksESPlorerForumRuActionPerformed() {//GEN-FIRST:event_MenuItemLinksESPlorerForumRuActionPerformed
        goLink(esp8266ru_uri);
    }//GEN-LAST:event_MenuItemLinksESPlorerForumRuActionPerformed

    private void MenuItemLinksESPlorerLatestActionPerformed() {//GEN-FIRST:event_MenuItemLinksESPlorerLatestActionPerformed
        goLink(esplorer_latest);
    }//GEN-LAST:event_MenuItemLinksESPlorerLatestActionPerformed

    private void MenuItemLinksESPlorerSourceActionPerformed() {//GEN-FIRST:event_MenuItemLinksESPlorerSourceActionPerformed
        goLink(esplorer_source);
    }//GEN-LAST:event_MenuItemLinksESPlorerSourceActionPerformed

    private void MenuItemLinksESPlorerHomeActionPerformed() {//GEN-FIRST:event_MenuItemLinksESPlorerHomeActionPerformed
        goLink(homepage_uri);
    }//GEN-LAST:event_MenuItemLinksESPlorerHomeActionPerformed

    private void MenuItemLinksDonateActionPerformed() {//GEN-FIRST:event_MenuItemLinksDonateActionPerformed
        goLink(donate_uri);
    }//GEN-LAST:event_MenuItemLinksDonateActionPerformed

    private void MenuItemLinksBuyOtherActionPerformed() {//GEN-FIRST:event_MenuItemLinksBuyOtherActionPerformed
        goLink(buy_other);
    }//GEN-LAST:event_MenuItemLinksBuyOtherActionPerformed

    private void MenuItemLinksDownloadLatestDevActionPerformed() {//GEN-FIRST:event_MenuItemLinksDownloadLatestDevActionPerformed
        goLink(nodemcu_download_dev_uri);
    }//GEN-LAST:event_MenuItemLinksDownloadLatestDevActionPerformed

    private void formWindowClosing() {//GEN-FIRST:event_formWindowClosing
        AppClose();
    }//GEN-LAST:event_formWindowClosing

    private void AppClose() {
        Rectangle r = this.getBounds();
        prefs.putInt(Constants.WIN_X, r.x);
        prefs.putInt(Constants.WIN_Y, r.y);
        prefs.putInt(Constants.WIN_H, r.height);
        prefs.putInt(Constants.WIN_W, r.width);
        //log("w="+Integer.toString(prefs.getInt(Constants.WIN_W, 0)));
        while (FilesTabbedPane.getTabCount() > 0) {
            if (CloseFile() == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if ((FilesTabbedPane.getTabCount() == 1) && isFileNew()) {
                break;
            }
        }
        this.setVisible(false);
        System.exit(0);
    }

    private void EOLItemStateChanged() {//GEN-FIRST:event_EOLItemStateChanged
        Terminal.setEOLMarkersVisible(EOL.isSelected());
    }//GEN-LAST:event_EOLItemStateChanged

    private void CondensedItemStateChanged() {//GEN-FIRST:event_CondensedItemStateChanged
        prefs.putBoolean(Constants.CONDENSED, Condensed.isSelected());
    }//GEN-LAST:event_CondensedItemStateChanged

    private void AutodetectFirmwareItemStateChanged() {//GEN-FIRST:event_AutodetectFirmwareItemStateChanged
        prefs.putBoolean(Constants.AUTODETECT, AutodetectFirmware.isSelected());
    }//GEN-LAST:event_AutodetectFirmwareItemStateChanged
    /*  Prefs end */

    private void MenuItemViewLF1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemViewLF1ActionPerformed
        String lclass = LAFclass.get(Integer.parseInt(evt.getActionCommand()));
        prefs.put("LAF", lclass);
        log("Set New LookAndFeel to:" + lclass);
        int isExit = Dialog("New LookAndFeel skin will be appled after program restart. Exit now?", JOptionPane.YES_NO_OPTION);
        if (isExit == JOptionPane.YES_OPTION) {
            AppClose();
        }
    }//GEN-LAST:event_MenuItemViewLF1ActionPerformed

    private void FileSystemInfo() {
        String cmd = "r,u,t=file.fsinfo() print(\"Total : \"..t..\" bytes\\r\\nUsed  : \"..u..\" bytes\\r\\nRemain: \"..r..\" bytes\\r\\n\") r=nil u=nil t=nil";
        LocalEcho = false;
        send(addCRLF(cmd), true);
    }

    private void goLink(URI link) {
        try {
            Desktop.getDesktop().browse(link);
        } catch (IOException ex) {
            Logger.getLogger(ESPlorer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void DoSnippet(int n) {
        //iSnippets = n;
        //SnippetName.setText(prefs.get("Snippet"+Integer.toString(n)+"name", "Snippet"+Integer.toString(n)));
        //SnippetText.setText(Snippets[n]);
        if (Condensed.isSelected()) {
            SendToESP(cmdPrep(Snippets[n]));
        } else {
            SendToESP(Snippets[n]);
        }
    }

    private void CommandsSetNodeMCU() {
        Command.removeAllItems();
        Command.addItem("=node.heap()");
        Command.addItem("=node.chipid()");
        Command.addItem("file.close()");
        Command.addItem("file.remove(\"\")");
        Command.addItem("dofile(\"\")");
        Command.addItem("wifi.setmode(wifi.STATION)");
        Command.addItem("wifi.setmode(wifi.SOFTAP)");
        Command.addItem("wifi.setmode(wifi.STATIONAP)");
        Command.addItem("=wifi.getmode()");
        Command.addItem("wifi.sta.config(\"myssid\",\"mypassword\")");
        Command.addItem("=wifi.sta.getip()");
        Command.addItem("=wifi.ap.getip()");
        Command.addItem("=wifi.sta.getmac()");
        Command.addItem("=wifi.ap.getmac()");
        Command.addItem("=wifi.sta.status()");
        Command.addItem("=tmr.now()");
    }

    private void CommandsSetAT() {
        Command.removeAllItems();
        Command.addItem("AT");
        Command.addItem("AT+GMR");
        Command.addItem("AT+RST");
    }

    private void inc_j() {
        j++;
    }

    private void j0() {
        j = 0;
    }

    private void PortFinder() {
        int i;
        Port.removeAllItems();
        if (UseCustomPortName.isSelected()) {
            Port.addItem(CustomPortName.getText().trim());
            Port.setSelectedIndex(Port.getItemCount() - 1);
            Port.setEnabled(false);
            Speed.setSelectedIndex(prefs.getInt(Constants.SERIAL_BAUD, 3));
            log("Using custom port name " + CustomPortName.getText());
            return;
        }
        log("Scan system...");
        String[] portNames = SerialPortList.getPortNames();
        //  System.out.println(portNames[i]);
        if (portNames.length < 1) {
            log("Could not find any serial port. Please, connect device and ReScan");
        } else {
            if (prefs.get(Constants.SERIAL_PORT, null) == null) {
                prefs.put(Constants.SERIAL_PORT, portNames[0]);
            }
            String lastPort = prefs.get(Constants.SERIAL_PORT, null);
            int port = 0;
            for (i = 0; i < portNames.length; i++) {
                Port.addItem(portNames[i]);
                if (portNames[i].equals(lastPort)) {
                    port = i;
                    log("found last saved serial port " + portNames[i]);
                } else {
                    log("found " + portNames[i]);
                }
            }
            Port.setSelectedIndex(port);
            Speed.setSelectedIndex(prefs.getInt(Constants.SERIAL_BAUD, 3));
        }
        log("Scan done.");
    }

    private void log(String l) {
        String log = Log.getText();
        Document doc = Log.getDocument();
        LOGGER.info(l);
        if (log.length() > LogMax) {
            try {
                doc.remove(0, 1024);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
        try {
            doc.insertString(doc.getLength(), "\r\n" + l, null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        Log.setCaretPosition(Log.getText().length());
    }

    private String GetSerialPortName() {
        String portName;
        if (UseCustomPortName.isSelected()) {
            portName = CustomPortName.getText().trim();
        } else {
            portName = Port.getSelectedItem().toString();
        }
        return portName;
    }

    private boolean SetSerialPortParams() {
        boolean success = false;
        String portName = GetSerialPortName();
        try {
            success = serialPort.setParams(nSpeed,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE,
                    PortRTS.isSelected(),
                    PortDTR.isSelected());
        } catch (Exception e) {
            log(e.toString());
        }
        if (!success) {
            log("ERROR setting port " + portName + " parameters.");
        }
        UpdateLED();
        return success;
    }

    private boolean portOpen() {
        boolean success;
        String portName = GetSerialPortName();
        nSpeed = Integer.parseInt((String) Speed.getSelectedItem());
        if (pOpen) {
            try {
                serialPort.closePort();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        } else {
            log("Try to open port " + portName + ", baud " + Integer.toString(nSpeed) + ", 8N1");
        }
        serialPort = new SerialPort(portName);
        pOpen = false;
        try {
            success = serialPort.openPort();
            if (!success) {
                log("ERROR open serial port " + portName);
            }
            if (success) {
                SetSerialPortParams();
                /*
                success = serialPort.setParams(nSpeed,
                                         SerialPort.DATABITS_8,
                                         SerialPort.STOPBITS_1,
                                         SerialPort.PARITY_NONE,
                                         PortRTS.isSelected(),
                                         PortDTR.isSelected());
                if (!success) {
                    log("ERROR setting port " + portName + " parameters.");
                }
                */
            }
            // This enables RTS as a side effect, and FLOWCONTROL_NONE is default anyway. Just skip it.
            /*
            if (success) {
                success = serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                if (!success) {
                    log("ERROR setting port " + portName + " NOFLOW control mode.");
                }
            }
            */
            if (success) {
                serialPort.addEventListener(new PortReader(), Constants.PORT_MASK);
            }
            if (success) {
                log("Open port " + portName + " - Success.");
            } else {
                log("Open port " + portName + " - FAIL.");
            }
        } catch (SerialPortException ex) {
            log(ex.toString());
            success = false;
        }
        pOpen = success;
        if (pOpen) {
            TerminalAdd("\r\nPORT OPEN " + Speed.getSelectedItem() + "\r\n");
            CheckComm();
        }
        return pOpen;

    }

    private void CheckComm() {
        if (!AutodetectFirmware.isSelected()) {
            portJustOpen = false;
            return;
        }
        portJustOpen = true;
        TerminalAdd("\r\nCommunication with MCU..");
        ActionListener checker = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (portJustOpen) {
                    btnSend("");  // only CR+LF
                    TerminalAdd(".");
                    if (Terminal.getCaretOffsetFromLineStart() >= 10) {
                        TerminalAdd("\r\n");
                    }
                } else {
                    openTimeout.stop();
                }
            }
        };
        openTimeout = new Timer(300, checker);
        openTimeout.setRepeats(true);
        openTimeout.setInitialDelay(0);
        openTimeout.start();
    }

    private void portClose() {
        boolean success = false;
        if (portJustOpen) {
            try {
                openTimeout.stop();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
        try {
            success = serialPort.closePort();
        } catch (SerialPortException ex) {
            log(ex.toString());
        }
        if (success) {
            TerminalAdd("\r\nPORT CLOSED\r\n");
            log("Close port " + Port.getSelectedItem().toString() + " - Success.");
        } else {
            log("Close port " + Port.getSelectedItem().toString() + " - unknown error.");

        }
        pOpen = false;
        if (Open.isSelected()) {
            Open.setSelected(false);
        }
        UpdateLED();
        ClearFileManager();
    }

    private String addCRLF(String s) {
        String r = s;
        r += (char) 13;
        r += (char) 10;
        return r;
    }

    private String addCR(String s) {
        String r = s;
        r += (char) 13;
        return r;
    }

    private void btnSend(String s) {
        send(addCRLF(s), true);
    }

    private void FinalInit() {
        final Image im = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/ESP8266-64x64.png"));
        setIconImage(im);

        setLocationRelativeTo(null); // window centered

        if (LAFclass.get(0).equals(prefs.get("LAF", ""))) {  // First for Nimbus
            MenuItemViewLF1.setSelected(true);
        }
        int x = 1;
        if (lookAndFeel.size() >= x + 1) {
            JRadioButtonMenuItem MenuItemViewLF2;
            MenuItemViewLF2 = new JRadioButtonMenuItem();
            buttonGroupLF.add(MenuItemViewLF2);
            MenuItemViewLF2.setText(lookAndFeel.get(x));
            if (LAFclass.get(x).equals(prefs.get("LAF", ""))) {
                MenuItemViewLF2.setSelected(true);
            }
            MenuItemViewLF2.setActionCommand(Integer.toString(x));
            MenuItemViewLF2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    MenuItemViewLF1ActionPerformed(evt);
                }
            });
            MenuView.add(MenuItemViewLF2);
        }
        x++;
        if (lookAndFeel.size() >= x + 1) {
            JRadioButtonMenuItem menuItemViewLF3 = new JRadioButtonMenuItem();
            buttonGroupLF.add(menuItemViewLF3);
            menuItemViewLF3.setText(lookAndFeel.get(x));
            if (LAFclass.get(x).equals(prefs.get("LAF", ""))) {
                menuItemViewLF3.setSelected(true);
            }
            menuItemViewLF3.setActionCommand(Integer.toString(x));
            menuItemViewLF3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    MenuItemViewLF1ActionPerformed(evt);
                }
            });
            MenuView.add(menuItemViewLF3);
        }
        x++;
        if (lookAndFeel.size() >= x + 1) {
            JRadioButtonMenuItem menuItemViewLF4 = new JRadioButtonMenuItem();
            buttonGroupLF.add(menuItemViewLF4);
            menuItemViewLF4.setText(lookAndFeel.get(x));
            if (LAFclass.get(x).equals(prefs.get("LAF", ""))) {
                menuItemViewLF4.setSelected(true);
            }
            menuItemViewLF4.setActionCommand(Integer.toString(x));
            menuItemViewLF4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    MenuItemViewLF1ActionPerformed(evt);
                }
            });
            MenuView.add(menuItemViewLF4);
        }
        x++;
        if (lookAndFeel.size() >= x + 1) {
            JRadioButtonMenuItem menuItemViewLF5 = new JRadioButtonMenuItem();
            buttonGroupLF.add(menuItemViewLF5);
            menuItemViewLF5.setText(lookAndFeel.get(x));
            if (LAFclass.get(x).equals(prefs.get("LAF", ""))) {
                menuItemViewLF5.setSelected(true);
            }
            menuItemViewLF5.setActionCommand(Integer.toString(x));
            menuItemViewLF5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    MenuItemViewLF1ActionPerformed(evt);
                }
            });
            MenuView.add(menuItemViewLF5);
        }
        x++;
        if (lookAndFeel.size() >= x + 1) {
            JRadioButtonMenuItem menuItemViewLF6 = new JRadioButtonMenuItem();
            buttonGroupLF.add(menuItemViewLF6);
            menuItemViewLF6.setText(lookAndFeel.get(x));
            if (LAFclass.get(x).equals(prefs.get("LAF", ""))) {
                menuItemViewLF6.setSelected(true);
            }
            menuItemViewLF6.setActionCommand(Integer.toString(x));
            menuItemViewLF6.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    MenuItemViewLF1ActionPerformed(evt);
                }
            });
            MenuView.add(menuItemViewLF6);
        }
        x++;
        if (lookAndFeel.size() >= x + 1) {
            JRadioButtonMenuItem menuItemViewLF7 = new JRadioButtonMenuItem();
            buttonGroupLF.add(menuItemViewLF7);
            menuItemViewLF7.setText(lookAndFeel.get(x));
            if (LAFclass.get(x).equals(prefs.get("LAF", ""))) {
                menuItemViewLF7.setSelected(true);
            }
            menuItemViewLF7.setActionCommand(Integer.toString(x));
            menuItemViewLF7.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    MenuItemViewLF1ActionPerformed(evt);
                }
            });
            MenuView.add(menuItemViewLF7);
        }
        x++;
        if (lookAndFeel.size() >= x + 1) {
            JRadioButtonMenuItem menuItemViewLF8 = new JRadioButtonMenuItem();
            buttonGroupLF.add(menuItemViewLF8);
            menuItemViewLF8.setText(lookAndFeel.get(x));
            if (LAFclass.get(x).equals(prefs.get("LAF", ""))) {
                menuItemViewLF8.setSelected(true);
            }
            menuItemViewLF8.setActionCommand(Integer.toString(x));
            menuItemViewLF8.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    MenuItemViewLF1ActionPerformed(evt);
                }
            });
            MenuView.add(menuItemViewLF8);
        }
        x++;
        if (lookAndFeel.size() >= x + 1) {
            JRadioButtonMenuItem menuItemViewLF9 = new JRadioButtonMenuItem();
            buttonGroupLF.add(menuItemViewLF9);
            menuItemViewLF9.setText(lookAndFeel.get(x));
            if (LAFclass.get(x).equals(prefs.get("LAF", ""))) {
                menuItemViewLF9.setSelected(true);
            }
            menuItemViewLF9.setActionCommand(Integer.toString(x));
            menuItemViewLF9.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    MenuItemViewLF1ActionPerformed(evt);
                }
            });
            MenuView.add(menuItemViewLF9);
        }

        LED_GREY = new ImageIcon(getClass().getResource("/resources/led_grey.png"));
        LED_GREEN = new ImageIcon(getClass().getResource("/resources/led_green.png"));
        LED_RED = new ImageIcon(getClass().getResource("/resources/led_red.png"));
        LED_BLUE = new ImageIcon(getClass().getResource("/resources/led_blue.png"));

        SnippetScrollPane.setLineNumbersEnabled(true);
        SnippetText.setFadeCurrentLineHighlight(true);
        SnippetText.setPaintMarkOccurrencesBorder(true);
        SnippetText.setPaintMatchedBracketPair(true);
        SnippetText.setAntiAliasingEnabled(true);
        SnippetText.setTabsEmulated(true);

        FileLayeredPane1 = new ArrayList<JLayeredPane>();
        TextScroll1 = new ArrayList<org.fife.ui.rtextarea.RTextScrollPane>();
        TextEditor1 = new ArrayList<org.fife.ui.rsyntaxtextarea.RSyntaxTextArea>();
        FileLayeredPaneLayout1 = new ArrayList<GroupLayout>();
        provider = new ArrayList<org.fife.ui.autocomplete.CompletionProvider>();
        ac = new ArrayList<org.fife.ui.autocomplete.AutoCompletion>();
        iFile = new ArrayList<File>();
        FileChanged = new ArrayList<Boolean>();
        FileAsButton = new ArrayList<JButton>();
        FilePopupMenu = new ArrayList<JPopupMenu>();
        FilePopupMenuItem = new ArrayList<JMenuItem>();

        FilesTabbedPane.removeAll();

        LoadPrefs();
        LoadSnippets();

        Terminal.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);

        AddTab(""); // iTab = 0

        try {
            donate_uri = new URI("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=4refr0nt%40gmail%2ecom&lc=US&item_name=ESPlorer&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted");
            homepage_uri = new URI("http://esp8266.ru/esplorer/");
            api_cn_uri = new URI("https://github.com/nodemcu/nodemcu-firmware/wiki/nodemcu_api_cn");
            api_en_uri = new URI("https://github.com/nodemcu/nodemcu-firmware/wiki/nodemcu_api_en");
            api_ru_uri = new URI("https://github.com/nodemcu/nodemcu-firmware/wiki/nodemcu_api_ru");
            changelog_uri = new URI("https://github.com/nodemcu/nodemcu-firmware/wiki");
            nodemcu_download_latest_uri = new URI("https://github.com/nodemcu/nodemcu-firmware/blob/master/pre_build/latest/nodemcu_latest.bin?raw=true");
            nodemcu_download_dev_uri = new URI("https://github.com/nodemcu/nodemcu-firmware/releases");
            flasher_uri = new URI("https://github.com/nodemcu/nodemcu-flasher");
            // adv links start
            // Please, do not modify
            buy_nodeMCU = new URI("http://ad.admitad.com/goto/1e8d114494955f13761d16525dc3e8/?subid=ESPlorer&ulp=http%3A%2F%2Fwww.aliexpress.com%2Faf%2Fnodemcu.html%3FSearchText%3Dnodemcu%26CatId%3D0%26shipCountry%3Dus%26initiative_id%3DSB_20150314041905%26isAffiliate%3Dy%26SortType%3Dprice_asc%26filterCat%3D400103%2C4099%26groupsort%3D1");
            buy_esp8266 = new URI("http://ad.admitad.com/goto/1e8d114494955f13761d16525dc3e8/?subid=ESPlorer&ulp=http%3A%2F%2Fwww.aliexpress.com%2Faf%2Fesp8266.html%3FSearchText%3Desp8266%26CatId%3D0%26shipCountry%3Dus%26initiative_id%3DSB_20150314041938%26isAffiliate%3Dy%26SortType%3Dprice_asc%26filterCat%3D400103%2C515%2C400107%26groupsort%3D1");
            buy_esd12 = new URI("http://ad.admitad.com/goto/1e8d114494955f13761d16525dc3e8/?subid=ESPlorer&ulp=http%3A%2F%2Fwww.aliexpress.com%2Faf%2Fesp8266-esd%2525252d12.html%3FSearchText%3Desp8266%252Besd-12%26CatId%3D0%26shipCountry%3Dall%26initiative_id%3DSB_20150314041646%26isAffiliate%3Dy%26isAtmOnline%3Dn%26SortType%3Dprice_asc");
            buy_other = new URI("http://ad.admitad.com/goto/1e8d114494955f13761d16525dc3e8/?subid=ESPlorer&ulp=http%3A%2F%2Fwww.aliexpress.com%2Faf%2Fcategory%2F5.html%3FshipCountry%3Dus%26isAffiliate%3Dy%26SortType%3Dtotal_tranpro_desc");
            // adv links end
            esp8266com_uri = new URI("http://www.esp8266.com/viewtopic.php?f=22&t=882");
            esp8266ru_uri = new URI("http://esp8266.ru/forum/threads/esplorer.34/");
            esplorer_latest = new URI("http://esp8266.ru/esplorer/#download");
            esplorer_source = new URI("https://github.com/4refr0nt/ESPlorer");

        } catch (Exception e) {
            log(e.toString());
        }
        FileAsButton1.setVisible(false);
        FileRenamePanel.setVisible(false);
    }

    private void LoadPrefs() {
//        log("Load saved settings...");
        // Settings - Firmware
        workDir = prefs.get(Constants.PATH, "");
        chooser = new JFileChooser(workDir);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setCurrentDirectory(new File(workDir));
        if (prefs.get(Constants.FIRMWARE, null) == null) {
            log("Load saved settings: NOT FOUND. Used default settings.");
            prefs.put(Constants.FIRMWARE, "NodeMCU");
            PrefsFlush();
        }
        if (prefs.get(Constants.FIRMWARE, "NodeMCU").equals("MicroPython") && OptionMicroPython.isEnabled()) {
            OptionMicroPython.setSelected(true);
            chooser.setFileFilter(FILTER_PYTHON);
        } else {
            OptionNodeMCU.setSelected(true);
            chooser.setFileFilter(FILTER_LUA);
        }
        FileAutoSaveDisk.setSelected(prefs.getBoolean(Constants.FILE_AUTO_SAVE_DISK, true));
        FileAutoSaveESP.setSelected(prefs.getBoolean(Constants.FILE_AUTO_SAVE_ESP, true));
        FileAutoRun.setSelected(prefs.getBoolean(Constants.FILE_AUTO_RUN, true));
        EditorTheme.setSelectedIndex(prefs.getInt(Constants.COLOR_THEME, 0));
        Delay.setValue(prefs.getInt(Constants.DELAY, 0));
        AnswerDelay.setValue(prefs.getInt(Constants.TIMEOUT, 3));
        DumbMode.setSelected(prefs.getBoolean(Constants.DUMB_MODE, false));
        TurboMode.setSelected(prefs.getBoolean(Constants.TURBO_MODE, false));
        LineDelay.setValue(prefs.getInt(Constants.LINE_DELAY, 200));
        // Font size
        Terminal.setFont(Terminal.getFont().deriveFont(prefs.getFloat(Constants.TERMINAL_FONT_SIZE, Constants.TERMINAL_FONT_SIZE_DEFAULT)));
        SnippetText.setFont(SnippetText.getFont().deriveFont(prefs.getFloat(Constants.EDITOR_FONT_SIZE, Constants.EDITOR_FONT_SIZE_DEFAULT)));
        Log.setFont(Log.getFont().deriveFont(prefs.getFloat(Constants.LOG_FONT_SIZE, Constants.LOG_FONT_SIZE_DEFAULT)));
        LogMax = prefs.getInt(Constants.LOG_MAX_SIZE, LogMax);
        LogMaxSize.setText(Integer.toString(LogMax / 1024));
        TerminalMax = prefs.getInt(Constants.TERMINAL_MAX_SIZE, TerminalMax);
        TerminalMaxSize.setText(Integer.toString(TerminalMax / 1024));
        AutoScroll.setSelected(prefs.getBoolean(Constants.AUTO_SCROLL, true));
        MenuItemViewLog.setSelected(prefs.getBoolean(Constants.SHOW_LOG, false));
        MenuItemViewToolbar.setSelected(prefs.getBoolean(Constants.SHOW_TOOLBAR, true));
        MenuItemViewLeftExtra.setSelected(prefs.getBoolean(Constants.SHOW_EXTRA_LEFT, true));
        MenuItemViewRightExtra.setSelected(prefs.getBoolean(Constants.SHOW_EXTRA_RIGHT, true));
        MenuItemViewSnippets.setSelected(prefs.getBoolean(Constants.SHOW_SNIP_RIGHT, true));
        MenuItemViewFileManager.setSelected(prefs.getBoolean(Constants.SHOW_FM_RIGHT, true));
        MenuItemViewDonate.setSelected(prefs.getBoolean(Constants.SHOW_DONATE, false));
        DonateSmall.setVisible(!MenuItemViewDonate.isSelected());
        UseCustomPortName.setSelected(prefs.getBoolean(Constants.USE_CUSTOM_PORT, false));
        CustomPortName.setText(prefs.get(Constants.CUSTOM_PORT_NAME, "/dev/AnySerialDevice"));
        PortDTR.setSelected(prefs.getBoolean(Constants.PORT_DTR, false));
        PortRTS.setSelected(prefs.getBoolean(Constants.PORT_RTS, false));
        UseExternalEditor.setSelected(prefs.getBoolean(Constants.USE_EXT_EDITOR, false));
        EOL.setSelected(prefs.getBoolean(Constants.SHOW_EOL, false));
        Condensed.setSelected(prefs.getBoolean(Constants.CONDENSED, false));
        AutodetectFirmware.setSelected(prefs.getBoolean(Constants.AUTODETECT, true));
        log("Load saved settings: DONE.");
    }

    private void AddFileButton(String FileName, int size) {
        FileAsButton.add(new JButton());
        int i = FileAsButton.size() - 1;
        FileAsButton.get(i).setText(FileName);
        //FileAsButton.get(i).setFont(new java.awt.Font("Tahoma", 0, 12));
        FileAsButton.get(i).setAlignmentX(0.5F);
        FileAsButton.get(i).setMargin(new java.awt.Insets(2, 2, 2, 2));
        FileAsButton.get(i).setMaximumSize(new java.awt.Dimension(130, 25));
        FileAsButton.get(i).setPreferredSize(new java.awt.Dimension(130, 25));
        FileAsButton.get(i).setHorizontalAlignment(SwingConstants.LEFT);
        //FileAsButton.get(i).setHorizontalTextPosition(SwingConstants.LEFT);
        FileAsButton.get(i).addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileAsButton1ActionPerformed(evt);
            }
        });
        // PopUp menu
        FilePopupMenu.add(new JPopupMenu());
        int x = FilePopupMenu.size() - 1;
        // PopUp menu items
        if (FileName.endsWith(".lua")) {
            FileAsButton.get(i).setIcon(new ImageIcon(getClass().getResource("/resources/lua.png")));
            FileAsButton.get(i).setToolTipText(FileAsButton.get(i).getActionCommand() + ", LeftClick - Run, RightClick - Other actions");
            AddMenuItemRun(x, FileName);
            AddMenuItemCompile(x, FileName);
            AddMenuItemSeparator(x);
            setupFileMenuItem(FileName, x, size);

        } else if (FileName.endsWith(".lc")) {
            FileAsButton.get(i).setIcon(new ImageIcon(getClass().getResource("/resources/lc.png")));
            FileAsButton.get(i).setToolTipText(FileAsButton.get(i).getActionCommand() + ", LeftClick - Run, RightClick - Other actions");
            AddMenuItemRun(x, FileName);
            AddMenuItemSeparator(x);
            AddMenuItemDump(x, FileName);
            AddMenuItemDownload(x, FileName, size);
            AddMenuItemRename(x, FileName);
            AddMenuItemSeparator(x);
            AddMenuItemRemove(x, FileName);
        } else {
            FileAsButton.get(i).setIcon(new ImageIcon(getClass().getResource("/resources/file.png")));
            FileAsButton.get(i).setToolTipText(FileAsButton.get(i).getActionCommand() + ", LeftClick - View, RightClick - Other actions");
            setupFileMenuItem(FileName, x, size);
            AddMenuItemDump(x, FileName);
            AddMenuItemEdit(x, FileName, size);
            AddMenuItemDownload(x, FileName, size);
            AddMenuItemRename(x, FileName);
            AddMenuItemSeparator(x);
            AddMenuItemRemove(x, FileName);
        }

        FileAsButton.get(i).setComponentPopupMenu(FilePopupMenu.get(x));
        FileManagerPane.add(FileAsButton.get(i));
    }

    private void setupFileMenuItem(String FileName, int x, int size) {
        AddMenuItemView(x, FileName);
        AddMenuItemDump(x, FileName);
        AddMenuItemEdit(x, FileName, size);
        AddMenuItemDownload(x, FileName, size);
        AddMenuItemRename(x, FileName);
        AddMenuItemSeparator(x);
        AddMenuItemRemove(x, FileName);
    }

    private void AddMenuItemSeparator(int x) {
        FilePopupMenu.get(x).add(new JPopupMenu.Separator());
    }

    private void AddMenuItemEdit(int x, String FileName, int size) {
        int y = addPopupItem();
        FilePopupMenuItem.get(y).setIcon(new ImageIcon(getClass().getResource("/resources/edit.png")));
        FilePopupMenuItem.get(y).setText("Edit " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("Download file from ESP and open in new editor window");
        FilePopupMenuItem.get(y).setActionCommand(FileName + "Size:" + Integer.toString(size));
        FilePopupMenuItem.get(y).addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DownloadCommand = "EDIT";
                FileDownload(evt.getActionCommand());
            }
        });
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemDownload(int x, String FileName, int size) {
        int y = addPopupItem();
        FilePopupMenuItem.get(y).setIcon(new ImageIcon(getClass().getResource("/resources/download.png")));
        FilePopupMenuItem.get(y).setText("Download " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("Download file from ESP and save to disk");
        FilePopupMenuItem.get(y).setActionCommand(FileName + "Size:" + Integer.toString(size));
        FilePopupMenuItem.get(y).addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DownloadCommand = "DOWNLOAD";
                FileDownload(evt.getActionCommand());
            }
        });
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemRun(int x, String FileName) {
        int y = addPopupItem();
        FilePopupMenuItem.get(y).setIcon(new ImageIcon(getClass().getResource("/resources/play.png")));
        FilePopupMenuItem.get(y).setText("Run " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("Execute command dofile(\"" + FileName + "\") for run this file");
        FilePopupMenuItem.get(y).setActionCommand(FileName);
        FilePopupMenuItem.get(y).addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSend("dofile(\"" + evt.getActionCommand() + "\")");
            }
        });
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemCompile(int x, String FileName) {
        int y = addPopupItem();
        FilePopupMenuItem.get(y).setIcon(new ImageIcon(getClass().getResource("/resources/wizard.png")));
        FilePopupMenuItem.get(y).setText("Compile " + FileName + " to .lc");
        FilePopupMenuItem.get(y).setToolTipText("Execute command node.compile(\"" + FileName + "\")");
        FilePopupMenuItem.get(y).setActionCommand(FileName);
        FilePopupMenuItem.get(y).addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSend("node.compile(\"" + evt.getActionCommand() + "\")");
                try {
                    Thread.sleep(500L);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getMessage());
                }
                FileListReload.doClick();
            }
        });
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemRename(int x, String FileName) {
        int y = addPopupItem();
        FilePopupMenuItem.get(y).setIcon(new ImageIcon(getClass().getResource("/resources/rename.png")));
        FilePopupMenuItem.get(y).setText("Rename " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("Execute command file.rename(\"" + FileName + "\",\"NewName\")");
        FilePopupMenuItem.get(y).setActionCommand(FileName);
        FilePopupMenuItem.get(y).addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileRename.setText(evt.getActionCommand());
                FileRenameLabel.setText(evt.getActionCommand());
                FileRenamePanel.setEnabled(true);
                FileRenamePanel.setVisible(true);
                FileRename.grabFocus();
            }
        });
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemRemove(int x, String FileName) {
        int y = addPopupItem();
        FilePopupMenuItem.get(y).setIcon(new ImageIcon(getClass().getResource("/resources/trash.png")));
        FilePopupMenuItem.get(y).setText("Remove " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("Execute command file.remove(\"" + FileName + "\") and delete file from NodeMCU filesystem");
        FilePopupMenuItem.get(y).setActionCommand(FileName);
        FilePopupMenuItem.get(y).addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileRemoveESP(evt.getActionCommand());
                // FileListReload not needed
            }
        });
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemView(int x, String FileName) {
        int y = addPopupItem();
        FilePopupMenuItem.get(y).setIcon(new ImageIcon(getClass().getResource("/resources/view.png")));
        FilePopupMenuItem.get(y).setText("View " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("View content of file " + FileName + " on Terminal");
        FilePopupMenuItem.get(y).setActionCommand(FileName);
        FilePopupMenuItem.get(y).addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewFile(evt.getActionCommand());
            }
        });
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private void AddMenuItemDump(int x, String FileName) {
        int y = addPopupItem();
        FilePopupMenuItem.get(y).setIcon(new ImageIcon(getClass().getResource("/resources/dump.png")));
        FilePopupMenuItem.get(y).setText("HexDump " + FileName);
        FilePopupMenuItem.get(y).setToolTipText("View HexDump " + FileName + "in Terminal");
        FilePopupMenuItem.get(y).setActionCommand(FileName);
        FilePopupMenuItem.get(y).addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HexDump(evt.getActionCommand());
            }
        });
        FilePopupMenu.get(x).add(FilePopupMenuItem.get(y));
    }

    private int addPopupItem() {
        int y;
        FilePopupMenuItem.add(new JMenuItem());
        y = FilePopupMenuItem.size() - 1;
        return y;
    }

    private void AddTab(String s) {
        int i = FilesTabbedPane.getTabCount();

        FileLayeredPane1.add(new JLayeredPane());
        TextScroll1.add(new org.fife.ui.rtextarea.RTextScrollPane());
        TextEditor1.add(new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea());
        iFile.add(new File(""));
        FileChanged.add(false);
        provider.add(createCompletionProvider());
        ac.add(new AutoCompletion(provider.get(i)));
        ac.get(i).install(TextEditor1.get(i));

        FileLayeredPaneLayout1.add(new GroupLayout(FileLayeredPane1.get(i)));

        TextEditor1.get(i).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
        TextEditor1.get(i).setColumns(20);
        TextEditor1.get(i).setRows(5);
        TextEditor1.get(i).setDragEnabled(false);
        TextEditor1.get(i).setFadeCurrentLineHighlight(true);
        TextEditor1.get(i).setPaintMarkOccurrencesBorder(true);
        TextEditor1.get(i).setPaintMatchedBracketPair(true);
        TextEditor1.get(i).setPopupMenu(ContextMenuEditor);
        TextEditor1.get(i).setCodeFoldingEnabled(false);
        TextEditor1.get(i).setAntiAliasingEnabled(true);
        TextEditor1.get(i).setTabsEmulated(true);
        TextEditor1.get(i).setBracketMatchingEnabled(true);
        TextEditor1.get(i).setTabSize(4);
        TextEditor1.get(i).addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                TextEditorCaretUpdate();
            }
        });
        TextEditor1.get(i).addActiveLineRangeListener(new org.fife.ui.rsyntaxtextarea.ActiveLineRangeListener() {
            public void activeLineRangeChanged(org.fife.ui.rsyntaxtextarea.ActiveLineRangeEvent evt) {
                TextEditorActiveLineRangeChanged();
            }
        });
        TextEditor1.get(i).addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                TextEditorCaretPositionChanged();
            }

            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        TextEditor1.get(i).addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextEditorKeyTyped();
            }
        });
        TextEditor1.get(i).addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                int i;
                for (i = 0; i < TextEditor1.size(); i++) {
                    TextEditor1.get(i).setEditable(!UseExternalEditor.isSelected());
                }
            }
        });
        TextScroll1.get(i).setViewportView(TextEditor1.get(i));
        TextScroll1.get(i).setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        TextScroll1.get(i).setIconRowHeaderEnabled(false);
        TextScroll1.get(i).setLineNumbersEnabled(true);
        TextScroll1.get(i).setFoldIndicatorEnabled(true);


        FileLayeredPane1.get(i).setLayout(FileLayeredPaneLayout1.get(i));
        FileLayeredPaneLayout1.get(i).setHorizontalGroup(
                FileLayeredPaneLayout1.get(i).createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(TextScroll1.get(i), GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
        FileLayeredPaneLayout1.get(i).setVerticalGroup(
                FileLayeredPaneLayout1.get(i).createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(TextScroll1.get(i), GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
        );
        FileLayeredPane1.get(i).setLayer(TextScroll1.get(i), JLayeredPane.DEFAULT_LAYER);

        FilesTabbedPane.addTab(NewFile, FileLayeredPane1.get(i));

        FilesTabbedPane.setSelectedIndex(i);
        iTab = i;
        SetTheme(EditorTheme.getSelectedIndex(), false);
        FileLabelUpdate();
        if (UseExternalEditor.isSelected()) {
            TextEditor1.get(i).setEditable(false);
        }
        TextEditor1.get(i).setText(s);
    }

    private void SetTheme(int t, boolean all) {
        String res;
        if (t == 1) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/dark.xml";
        } else if (t == 2) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/eclipse.xml";
        } else if (t == 3) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/idea.xml";
        } else if (t == 4) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/vs.xml";
        } else if (t == 5) {
            res = "/org/fife/ui/rsyntaxtextarea/themes/default-alt.xml";
        } else {
            res = "/org/fife/ui/rsyntaxtextarea/themes/default.xml";
        }
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(res));
            if (all) {
                for (int i = 0; i < FilesTabbedPane.getTabCount(); i++) {
                    theme.apply(TextEditor1.get(i));
                    TextEditor1.get(i).setFont(TextEditor1.get(i).getFont().deriveFont(prefs.getFloat(Constants.EDITOR_FONT_SIZE, Constants.EDITOR_FONT_SIZE_DEFAULT)));
                }
                theme.apply(SnippetText);
                SnippetText.setFont(SnippetText.getFont().deriveFont(prefs.getFloat(Constants.EDITOR_FONT_SIZE, Constants.EDITOR_FONT_SIZE_DEFAULT)));
                theme.apply(Terminal);
                Terminal.setFont(Terminal.getFont().deriveFont(prefs.getFloat(Constants.TERMINAL_FONT_SIZE, Constants.TERMINAL_FONT_SIZE_DEFAULT)));
                themeTextBackground = SnippetText.getBackground();
                SnippetText.setBackground(SnippetTopPane.getBackground());
                log("Set new color theme: Success.");
            } else {
                theme.apply(TextEditor1.get(iTab));
                TextEditor1.get(iTab).setFont(TextEditor1.get(iTab).getFont().deriveFont(prefs.getFloat(Constants.EDITOR_FONT_SIZE, Constants.EDITOR_FONT_SIZE_DEFAULT)));
            }
        } catch (IOException e) {
            log(e.toString());
            log("Set new color theme: FAIL.");
        }
    }

    private void RemoveTab() {
        if (FilesTabbedPane.getTabCount() <= 1) {
            iTab = 0;
            TextEditor1.get(iTab).setText("");
            TextEditor1.get(iTab).discardAllEdits();
            FilesTabbedPane.setTitleAt(iTab, NewFile);
            iFile.set(iTab, new File(""));
            FileLabelUpdate();
            FileChanged.set(iTab, false);
            UpdateEditorButtons();
            log("FileTab cleared: Success.");
        } else {
            iFile.remove(iTab);
            FileChanged.remove(iTab);
            ac.remove(iTab);
            provider.remove(iTab);
            TextEditor1.remove(iTab);
            TextScroll1.remove(iTab);
            FileLayeredPaneLayout1.remove(iTab);
            FileLayeredPane1.remove(iTab);
            FilesTabbedPane.removeTabAt(iTab);
            FilesTabbedPane.setSelectedIndex(iTab);
            FileLabelUpdate();
            log("FileTab removed: Success.");
        }
    }

    private boolean isChanged() {
        if (UseExternalEditor.isSelected()) {
            return false;
        }
        try {
            if (FileChanged.get(iTab)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return TextEditor1.get(iTab).canUndo() || TextEditor1.get(iTab).canRedo();
    }

    private int Dialog(String msg, int btn) {
        this.setAlwaysOnTop(false);
        Toolkit.getDefaultToolkit().beep();
        int returnVal = JOptionPane.showConfirmDialog(null, msg, "Attention", btn, JOptionPane.WARNING_MESSAGE);
        this.setAlwaysOnTop(AlwaysOnTop.isSelected());
        return returnVal;
    }

    private org.fife.ui.autocomplete.CompletionProvider createCompletionProvider() {

        org.fife.ui.autocomplete.DefaultCompletionProvider provider = new DefaultCompletionProvider();

        provider.addCompletion(new BasicCompletion(provider, "function end"));
        provider.addCompletion(new BasicCompletion(provider, "function"));
        provider.addCompletion(new BasicCompletion(provider, "function return end"));
        provider.addCompletion(new BasicCompletion(provider, "end"));
        provider.addCompletion(new BasicCompletion(provider, "do"));
        provider.addCompletion(new BasicCompletion(provider, "print(\"\")"));
        provider.addCompletion(new BasicCompletion(provider, "if"));
        provider.addCompletion(new BasicCompletion(provider, "else"));
        provider.addCompletion(new BasicCompletion(provider, "elseif"));
        provider.addCompletion(new BasicCompletion(provider, "if else end"));
        provider.addCompletion(new BasicCompletion(provider, "while"));
        provider.addCompletion(new BasicCompletion(provider, "while do end"));
        provider.addCompletion(new BasicCompletion(provider, "do end"));
        provider.addCompletion(new BasicCompletion(provider, "for"));
        provider.addCompletion(new BasicCompletion(provider, "for do end"));
        provider.addCompletion(new BasicCompletion(provider, "repeat"));
        provider.addCompletion(new BasicCompletion(provider, "until"));
        provider.addCompletion(new BasicCompletion(provider, "repeat until"));
        provider.addCompletion(new BasicCompletion(provider, "for"));
        provider.addCompletion(new BasicCompletion(provider, "for key, value in pairs() do\r\nend"));
        provider.addCompletion(new BasicCompletion(provider, "for do end"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.setmode(wifi.STATION)"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.getmode()"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.startsmart()"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.stopsmart()"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.sta.config(\"SSID\",\"password\")"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.sta.connect()"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.sta.disconnect()"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.sta.autoconnect()"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.sta.getip()"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.sta.getmac()"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.sta.getap()"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.sta.status()"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.ap.config()"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.ap.getip()"));
        provider.addCompletion(new BasicCompletion(provider, "wifi.ap.getmac()"));
        provider.addCompletion(new BasicCompletion(provider, "gpio.mode(pin,gpio.OUTPUT)"));
        provider.addCompletion(new BasicCompletion(provider, "gpio.write(pin,gpio.HIGH)"));
        provider.addCompletion(new BasicCompletion(provider, "gpio.write(pin,gpio.LOW)"));
        provider.addCompletion(new BasicCompletion(provider, "gpio.read(pin)"));
        provider.addCompletion(new BasicCompletion(provider, "gpio.trig(0, \"act\",func)"));
        provider.addCompletion(new BasicCompletion(provider, "conn=net.createConnection(net.TCP, 0)"));
        provider.addCompletion(new BasicCompletion(provider, "net.createConnection(net.TCP, 0)"));
        provider.addCompletion(new BasicCompletion(provider, "on(\"receive\", function(conn, payload) print(payload) end )"));
        provider.addCompletion(new BasicCompletion(provider, "connect(80,\"0.0.0.0\")"));
        provider.addCompletion(new BasicCompletion(provider, "send(\"GET / HTTP/1.1\\r\\nHost: www.baidu.com\\r\\nConnection: keep-alive\\r\\nAccept: */*\\r\\n\\r\\n\")"));
        provider.addCompletion(new BasicCompletion(provider, "srv=net.createServer(net.TCP)"));
        provider.addCompletion(new BasicCompletion(provider, "srv:listen(80,function(conn) \nconn:on(\"receive\",function(conn,payload) \nprint(payload) \nconn:send(\"<h1> Hello, NodeMcu.</h1>\")\nend) \nconn:on(\"sent\",function(conn) conn:close() end)\nend)"));
        provider.addCompletion(new BasicCompletion(provider, "net.createServer(net.TCP, timeout)"));
        provider.addCompletion(new BasicCompletion(provider, "net.server.listen(port,[ip],function(net.socket))"));
        provider.addCompletion(new BasicCompletion(provider, "dns(domain, function(net.socket, ip))"));
        provider.addCompletion(new BasicCompletion(provider, "pwm.setduty(0,0)"));
        provider.addCompletion(new BasicCompletion(provider, "pwm.getduty(0)"));
        provider.addCompletion(new BasicCompletion(provider, "pwm.setup(0,0,0)"));
        provider.addCompletion(new BasicCompletion(provider, "pwm.start(0)"));
        provider.addCompletion(new BasicCompletion(provider, "pwm.close(0)"));
        provider.addCompletion(new BasicCompletion(provider, "pwm.setclock(0, 100)"));
        provider.addCompletion(new BasicCompletion(provider, "pwm.getclock(0)"));
        provider.addCompletion(new BasicCompletion(provider, "pwm.close(0)"));
        provider.addCompletion(new BasicCompletion(provider, "file.open(\"\",\"r\")"));
        provider.addCompletion(new BasicCompletion(provider, "file.writeline()"));
        provider.addCompletion(new BasicCompletion(provider, "file.readline()"));
        provider.addCompletion(new BasicCompletion(provider, "file.write()"));
        provider.addCompletion(new BasicCompletion(provider, "file.close()"));
        provider.addCompletion(new BasicCompletion(provider, "file.remove()"));
        provider.addCompletion(new BasicCompletion(provider, "file.flush()"));
        provider.addCompletion(new BasicCompletion(provider, "file.seek()"));
        provider.addCompletion(new BasicCompletion(provider, "file.list()"));
        provider.addCompletion(new BasicCompletion(provider, "node.restart()"));
        provider.addCompletion(new BasicCompletion(provider, "node.dsleep()"));
        provider.addCompletion(new BasicCompletion(provider, "node.chipid()"));
        provider.addCompletion(new BasicCompletion(provider, "node.heap()"));
        provider.addCompletion(new BasicCompletion(provider, "node.key(type, function())"));
        provider.addCompletion(new BasicCompletion(provider, "node.led()"));
        provider.addCompletion(new BasicCompletion(provider, "node.input()"));
        provider.addCompletion(new BasicCompletion(provider, "node.output()"));
        provider.addCompletion(new BasicCompletion(provider, "tmr.alarm(0,1000,1,function()\nend)"));
        provider.addCompletion(new BasicCompletion(provider, "tmr.delay()"));
        provider.addCompletion(new BasicCompletion(provider, "tmr.now()"));
        provider.addCompletion(new BasicCompletion(provider, "tmr.stop(id)"));
        provider.addCompletion(new BasicCompletion(provider, "tmr.wdclr()"));
        provider.addCompletion(new BasicCompletion(provider, "dofile(\"\")"));
      /*
      provider.addCompletion(new ShorthandCompletion(provider, "sysout",
            "System.out.println(", "System.out.println("));
      provider.addCompletion(new ShorthandCompletion(provider, "syserr",
            "System.err.println(", "System.err.println("));
      */
        return provider;

    }

    private void FileLabelUpdate() {
        iTab = FilesTabbedPane.getSelectedIndex();
        if (isFileNew()) {
            FilePathLabel.setText("");
        } else {
            try {
                if (UseExternalEditor.isSelected()) {
                    FilePathLabel.setText("ReadOnly " + iFile.get(FilesTabbedPane.getSelectedIndex()).getPath());
                } else {
                    FilePathLabel.setText(iFile.get(FilesTabbedPane.getSelectedIndex()).getPath());
                }
            } catch (Exception e) {
                FilePathLabel.setText("");
            }
        }
        UpdateEditorButtons();
    }

    private int CloseFile() {
        if (UseExternalEditor.isSelected()) {
            return JOptionPane.YES_OPTION;
        }
        if (isChanged()) {
            log("File changed. Ask before closing.");
            int returnVal = Dialog("Save file \"" + FilesTabbedPane.getTitleAt(iTab) + "\" before closing?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (returnVal == JOptionPane.YES_OPTION) {
                if (!SaveFile()) {
                    log("File close: FAIL (file not saved, closing aborted)");
                    return JOptionPane.CANCEL_OPTION;
                }
            } else if (returnVal == JOptionPane.CANCEL_OPTION) {
                log("User select: Continue editing.");
                return JOptionPane.CANCEL_OPTION;
            } else {
                log("User select: Close anyway.");
            }
        }
        RemoveTab();
        log("File close: Success.");
        return JOptionPane.YES_OPTION;
    }

    private void ReloadFile() {
        if (isFileNew()) {
            return;
        }
        if (isChanged() && !UseExternalEditor.isSelected()) {
            log("File reload: File changed. Ask before reloading.");
            int returnVal = Dialog("Discard any changes and reload file from disk?", JOptionPane.YES_NO_OPTION);
            if (returnVal != JOptionPane.OK_OPTION) {
                log("File reload: FAIL (file not saved, reload cancelled by user choice)");
                return;
            } else {
                log("File reload: Reload anyway by user choice.");
            }
        }
        if (LoadFile()) {
            log("File reload: Success.");
        }
    }

    private void StopSend() {
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        try {
            timer.stop();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        try {
            timeout.stop();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        try {
            serialPort.addEventListener(new PortReader(), Constants.PORT_MASK);
        } catch (SerialPortException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        SendUnLock();
        long duration = System.currentTimeMillis() - startTime;
        log("Operation done. Duration = " + Long.toString(duration) + " ms");
    }

    private void SendToESP(String str) {
        if (!pOpen || portJustOpen) {
            log("SendESP: Serial port not open. Cancel.");
            return;
        }
        sendBuf = new ArrayList<String>();
        s = str.split("\r?\n");
        sendBuf.addAll(Arrays.asList(s));
        SendTimerStart();
        log("SendToESP: Starting...");
    }

    private void SendToESP(ArrayList<String> buf) {
        if (!pOpen || portJustOpen) {
            log("SendESP: Serial port not open. Cancel.");
            return;
        }
        sendBuf = new ArrayList<String>();
        sendBuf.addAll(buf);
        SendTimerStart();
        log("SendToESP: Starting...");
    }

    private void WatchDog() {
        if (DumbMode.isSelected()) {
            return;
        }
        ActionListener watchDog = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                StopSend();
                Toolkit.getDefaultToolkit().beep();
                TerminalAdd("Waiting answer from ESP - Timeout reached. Command aborted.");
                log("Waiting answer from ESP - Timeout reached. Command aborted.");
            }
        };
        int delay = AnswerDelay.getValue() * 1000;
        if (delay == 0) delay = 300;
        timeout = new Timer(delay, watchDog);
        timeout.setRepeats(false);
        timeout.setInitialDelay(delay);
        timeout.start();
    }

    private void SaveFileESP() {
        if (!pOpen || portJustOpen) {
            log("FileSaveESP: Serial port not open. Cancel.");
            return;
        }
        log("FileSaveESP: Try to save file to ESP...");
        String ft = iFile.get(iTab).getName();
        if (ft.length() == 0) {
            log("FileSaveESP: FAIL. Can't save file to ESP without name.");
            SendUnLock();
            JOptionPane.showMessageDialog(null, "Can't save file to ESP without name.");
            return;
        }
        sendBuf = new ArrayList<String>();
        if (TurboMode.isSelected()) {
            SaveFileESPTurbo(ft);
        }
        sendBuf.add("file.remove(\"" + ft + "\");");
        sendBuf.add("file.open(\"" + ft + "\",\"w+\");");
        sendBuf.add("w = file.writeline\r\n");
        s = TextEditor1.get(iTab).getText().split("\r?\n");
        for (String subs : s) {
            sendBuf.add("w([[" + subs + "]]);");
        }
        sendBuf.add("file.close();");
        if (FileAutoRun.isSelected()) {
            sendBuf.add("dofile(\"" + ft + "\");");
        }
        // data ready
        SendTimerStart();
        log("FileSaveESP: Starting...");
    }

    private void SaveFileESPTurbo(String ft) {
        log("FileSaveESP-Turbo: Try to save file to ESP in Turbo Mode...");
        sendBuf.add("local FILE=\"" + ft + "\" file.remove(FILE) file.open(FILE,\"w+\") uart.setup(0," + Integer.toString(nSpeed) + ",8,0,1,0)");
        sendBuf.add("ESP_Receiver=function(rcvBuf) if string.match(rcvBuf,\"^ESP_cmd_close\")==nil then file.write(string.gsub(rcvBuf, \'\\r\', \'\')) uart.write(0, \"> \") else uart.on(\"data\") ");
        sendBuf.add("file.flush() file.close() FILE=nil rcvBuf=nil ESP_Receiver=nil uart.setup(0," + Integer.toString(nSpeed) + ",8,0,1,1) str=\"\\r\\n--Done--\\r\\n> \" print(str) str=nil collectgarbage() end end uart.on(\"data\",'\\r',ESP_Receiver,0)");
        int pos1 = 0;
        int pos2;
        int size = 254;
        int l = TextEditor1.get(iTab).getText().length();
        String fragment;
        while (pos1 <= l) {
            pos2 = pos1 + size;
            if (pos2 > l) pos2 = l;
            fragment = TextEditor1.get(iTab).getText().substring(pos1, pos2);
            sendBuf.add(fragment);
            pos1 += size;
        }
        sendBuf.add("ESP_cmd_close");
        sendBuf.add("\r\n");
        if (FileAutoRun.isSelected()) {
            sendBuf.add("dofile(\"" + ft + "\")");
        }
        SendTurboTimerStart();
        log("FileSaveESP-Turbo: Starting...");
    }

    private void SendTurboTimerStart() {
        startTime = System.currentTimeMillis();
        SendLock();
        rcvBuf = "";
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        try {
            serialPort.addEventListener(new PortTurboReader(), Constants.PORT_MASK);
        } catch (SerialPortException e) {
            log("DataTurboSender: Add EventListener Error. Canceled.");
            return;
        }
        int delay;
        j0();
        delay = Delay.getValue();
        if (delay == 0) delay = 10;
        taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (j < sendBuf.size()) {
                    send(addCR(sendBuf.get(j)), false);
                }
            }
        };
        timer = new Timer(delay, taskPerformer);
        timer.setRepeats(false);
        log("DataTurboSender: start \"Smart Mode\"");
        timer.setInitialDelay(delay);
        WatchDog();
        timer.start();
    }

    private void SendTimerStart() {
        startTime = System.currentTimeMillis();
        SendLock();
        rcvBuf = "";
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        try {
            if (DumbMode.isSelected()) {
                serialPort.addEventListener(new PortReader(), Constants.PORT_MASK);
            } else {
                serialPort.addEventListener(new PortExtraReader(), Constants.PORT_MASK);
            }
        } catch (SerialPortException e) {
            log("DataSender: Add EventListener Error. Canceled.");
            return;
        }
        int delay;
        j0();
        if (DumbMode.isSelected()) { // DumbMode
            delay = LineDelay.getValue();
            taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (j < sendBuf.size()) {
                        send(addCRLF(sendBuf.get(j).trim()), false);
                        inc_j();
                        int div = sendBuf.size() - 1;
                        if (div == 0) div = 1; // for non-zero divide
                        ProgressBar.setValue((j * 100) / div);
                        if (j > sendBuf.size() - 1) {
                            timer.stop();
                            StopSend();
                        }
                    }
                }
            };
            timer = new Timer(delay, taskPerformer);
            timer.setRepeats(true);
            timer.setInitialDelay(delay);
            timer.start();
            log("DataSender: start \"Dumb Mode\"");
        } else { // SmartMode
            delay = Delay.getValue();
            if (delay == 0) delay = 10;
            taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (j < sendBuf.size()) {
                        send(addCRLF(sendBuf.get(j).trim()), false);
                    }
                }
            };
            timer = new Timer(delay, taskPerformer);
            timer.setRepeats(false);
            timer.setInitialDelay(delay);
            timer.start();
            log("DataSender: start \"Smart Mode\"");
            WatchDog();
        }
    }

    private void send(String s, boolean simple) {
        if (!pOpen) {
            log("DataSender: Serial port not open, operation FAILED.");
            return;
        }
        if (busyIcon) {
            Busy.setIcon(LED_BLUE);
            SnippetsBusy.setIcon(LED_BLUE);
        } else {
            Busy.setIcon(LED_RED);
            SnippetsBusy.setIcon(LED_RED);
        }
        busyIcon = !busyIcon;
        try {
            log("sending:" + s.replace("\r\n", "<CR><LF>"));
            serialPort.writeString(s);
        } catch (SerialPortException ex) {
            log("send FAIL:" + s.replace("\r\n", "<CR><LF>"));
        }
        if (!DumbMode.isSelected() && !simple) {
            try {
                timeout.restart();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
        if (simple) {
            Busy.setIcon(LED_GREY);
            SnippetsBusy.setIcon(LED_GREY);
        }
    }

    private void Busy() {
        Busy.setText("BUSY");
        Busy.setBackground(new java.awt.Color(153, 0, 0)); // RED
        SnippetsBusy.setText("BUSY");
        SnippetsBusy.setBackground(new java.awt.Color(153, 0, 0)); // RED
        ProgressBar.setValue(0);
        ProgressBar.setVisible(true);
        FileSendESP.setEnabled(false);
        MenuItemFileSendESP.setEnabled(false);
        MenuItemFileRemoveESP.setEnabled(false);
        NodeReset.setEnabled(false);
        FileDo.setEnabled(false);
        MenuItemFileDo.setEnabled(false);
        MenuItemEditSendSelected.setEnabled(false);
        MenuItemEditorSendSelected.setEnabled(false);
        ButtonSendSelected.setEnabled(false);
        MenuItemEditSendLine.setEnabled(false);
        MenuItemEditorSendLine.setEnabled(false);
        ButtonSendLine.setEnabled(false);
        SnippetRun.setEnabled(false);
        ButtonSnippet0.setEnabled(false);
        ButtonSnippet1.setEnabled(false);
        ButtonSnippet2.setEnabled(false);
        ButtonSnippet3.setEnabled(false);
        ButtonSnippet4.setEnabled(false);
        ButtonSnippet5.setEnabled(false);
        ButtonSnippet6.setEnabled(false);
        ButtonSnippet7.setEnabled(false);
        ButtonSnippet8.setEnabled(false);
        ButtonSnippet9.setEnabled(false);
        ButtonSnippet10.setEnabled(false);
        ButtonSnippet11.setEnabled(false);
        ButtonSnippet12.setEnabled(false);
        ButtonSnippet13.setEnabled(false);
        ButtonSnippet14.setEnabled(false);
        ButtonSnippet15.setEnabled(false);
    }

    private void SendLock() {
        Busy();
        FileSaveESP.setText("Cancel");
        FileSaveESP.setIcon(new ImageIcon(getClass().getResource("/resources/abort.png")));
        FileSaveESP.setSelected(true);
    }

    private void Idle() {
        Busy.setText("IDLE");
        Busy.setBackground(new java.awt.Color(0, 153, 0)); // GREEN
        Busy.setIcon(LED_GREY);
        SnippetsBusy.setText("IDLE");
        SnippetsBusy.setBackground(new java.awt.Color(0, 153, 0)); // GREEN
        SnippetsBusy.setIcon(LED_GREY);
        ProgressBar.setVisible(false);
        FileSendESP.setSelected(true);
        UpdateButtons();
        UpdateEditorButtons();
    }

    private void UpdateLED() {
        pOpen = Open.isSelected();
        if (!pOpen) {
            PortDTR.setIcon(LED_GREY);
            PortRTS.setIcon(LED_GREY);
            PortCTS.setIcon(LED_GREY);
            Open.setText("Open");
            Open.setIcon(new ImageIcon(getClass().getResource("/resources/connect1.png")));
            PortOpenLabel.setIcon(LED_GREY);
            return;
        }
        Open.setText("Close");
        Open.setIcon(new ImageIcon(getClass().getResource("/resources/disconnect1.png")));
        PortOpenLabel.setIcon(LED_GREEN);
        UpdateLedCTS();
        if (PortDTR.isSelected()) {
            PortDTR.setIcon(LED_GREEN);
        } else {
            PortDTR.setIcon(LED_GREY);
        }
        if (PortRTS.isSelected()) {
            PortRTS.setIcon(LED_GREEN);
        } else {
            PortRTS.setIcon(LED_GREY);
        }
        if (portJustOpen) {
            PortOpenLabel.setIcon(LED_RED);
        }
    }

    private void SendUnLock() {
        Idle();
        FileSaveESP.setText("Save to ESP");
        FileSaveESP.setIcon(new ImageIcon(getClass().getResource("/resources/move.png")));
        FileSaveESP.setSelected(false);
        FileSendESP.setSelected(false);
    }

    private void SetSnippetEditButtonsTooltip() {
        // should be called after loading, setting or editing any of the Snippet-Names
        // eg after SnippetSaveActionPerformed() and LoadSnippets ()
        // Mike, DL2ZAP 2015-01-04
        SnippetEdit0.setToolTipText(ButtonSnippet0.getText());
        SnippetEdit1.setToolTipText(ButtonSnippet1.getText());
        SnippetEdit2.setToolTipText(ButtonSnippet2.getText());
        SnippetEdit3.setToolTipText(ButtonSnippet3.getText());
        SnippetEdit4.setToolTipText(ButtonSnippet4.getText());
        SnippetEdit5.setToolTipText(ButtonSnippet5.getText());
        SnippetEdit6.setToolTipText(ButtonSnippet6.getText());
        SnippetEdit7.setToolTipText(ButtonSnippet7.getText());
        SnippetEdit8.setToolTipText(ButtonSnippet8.getText());
        SnippetEdit9.setToolTipText(ButtonSnippet9.getText());
        SnippetEdit10.setToolTipText(ButtonSnippet10.getText());
        SnippetEdit11.setToolTipText(ButtonSnippet11.getText());
        SnippetEdit13.setToolTipText(ButtonSnippet13.getText());
        SnippetEdit13.setToolTipText(ButtonSnippet13.getText());
        SnippetEdit14.setToolTipText(ButtonSnippet14.getText());
        SnippetEdit15.setToolTipText(ButtonSnippet15.getText());
    }

    private void SaveDownloadedFile() {
        log("Saving downloaded file...");
//            FileCount ++;
//            iFile.set(iTab, new File("script" + Integer.toString(FileCount) + ".lua") );
        chooser.rescanCurrentDirectory();
        File f = new File(DownloadedFileName);
        javax.swing.filechooser.FileFilter flt = chooser.getFileFilter();
        chooser.resetChoosableFileFilters();
        chooser.setSelectedFile(f);
        chooser.setDialogTitle("Save downloaded from ESP file \"" + DownloadedFileName + "\" As...");
        int returnVal = chooser.showSaveDialog(null);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            log("Saving abort by user.");
            return;
        }
        f = chooser.getSelectedFile();
        chooser.setFileFilter(flt);
        DownloadedFileName = f.getName();
        SavePath();
        if (f.exists()) {
            log("File " + DownloadedFileName + " already exist, waiting user choice");
            int shouldWrite = Dialog("File " + DownloadedFileName + " already exist. Overwrite?", JOptionPane.YES_NO_OPTION);
            if (shouldWrite != JOptionPane.YES_OPTION) {
                log("Saving canceled by user, because file " + DownloadedFileName + " already exist");
                return;
            } else {
                log("File " + DownloadedFileName + " will be overwriten by user choice");
            }
        } else { // we saving file, when open
            log("We saving new file " + DownloadedFileName);
        }
        try {
            log("Try to saving file " + DownloadedFileName + " ...");
            fos = new FileOutputStream(f);
            fos.write(PacketsByte);
            fos.flush();
            log("Save file " + DownloadedFileName + ": Success, size:" + Long.toString(f.length()));
        } catch (Exception e) {
            log("Save file " + DownloadedFileName + ": FAIL.");
            log(e.toString());
            JOptionPane.showMessageDialog(null, "Error, file " + DownloadedFileName + " not saved!");
        }
        try {
            if (fos != null) fos.close();
        } catch (IOException e) {
            log(e.toString());
        }
    }

    private void UploadFiles() {
        if (!pOpen) {
            log("Uploader: Serial port not open, operation FAILED.");
            return;
        }
        if (portJustOpen) {
            log("Uploader: Communication with MCU not yet established.");
            return;
        }
        chooser.rescanCurrentDirectory();
        javax.swing.filechooser.FileFilter flt = chooser.getFileFilter();
        chooser.resetChoosableFileFilters();
        chooser.setDialogTitle("Select file to upload to ESP");
        //chooser.setMultiSelectionEnabled(true);
        int returnVal = chooser.showOpenDialog(LeftBasePane);
        mFile = new ArrayList<File>();
        log("Uploader: chooser selected file:" + chooser.getSelectedFiles().length);
//        if ( mFile.addAll(Arrays.asList(chooser.getSelectedFiles())) ) {
        if (mFile.add(chooser.getSelectedFile())) {
            mFileIndex = 0;
        } else {
            mFileIndex = -1;
            log("Uploader: no file selected");
            return;
        }
        chooser.setFileFilter(flt);
        chooser.setMultiSelectionEnabled(false);
        if (!(returnVal == JFileChooser.APPROVE_OPTION)) {
            log("Uploader: canceled by user");
            return;
        }
        SavePath();
        UploadFilesStart();
    }

    private void UploadFilesStart() {
        String uploadFileName = mFile.get(mFileIndex).getName();
        sendBuf = new ArrayList<String>();
        PacketsData = new ArrayList<String>();
        PacketsCRC = new ArrayList<Integer>();
        PacketsSize = new ArrayList<Integer>();
        PacketsNum = new ArrayList<Integer>();
        sendPacketsCRC = new ArrayList<Boolean>();
        rcvBuf = "";
        PacketsByte = new byte[0];
        rx_byte = new byte[0];
        tx_byte = new byte[0];

        if (!LoadBinaryFile(mFile.get(mFileIndex))) {
            log("Uploader: loaded fail!");
            return;
        }
        int lastPacketSize = SplitDataToPackets();
        if (lastPacketSize < 0) {
            log("Uploader: SplitDataToPackets fail!");
            return;
        }
        log("sendPackets=" + Integer.toString(sendPackets.size()));
        String cmd = "_up=function(n,l,ll)\n" +
                "     local cs = 0\n" +
                "     local i = 0\n" +
                "     print(\">\"..\" \")\n" +
                "     uart.on(\"data\", l, function(b) \n" +
                "          i = i + 1\n" +
                "          file.open(\"" + uploadFileName + "\",'a+')\n" +
                "          file.write(b)\n" +
                "          file.close()\n" +
                "          cs=0\n" +
                "          for j=1, l do\n" +
                "               cs = cs + (b:byte(j)*20)%19\n" +
                "          end\n" +
                "          uart.write(0,\"~~~CRC-\"..\"START~~~\"..cs..\"~~~CRC-\"..\"END~~~\")\n" +
                "          if i == n then\n" +
                "               uart.on(\"data\")\n" +
                "          end\n" +
                "          if i == n-1 and ll>0 then\n" +
                "               _up(1,ll,ll)\n" +
                "          end\n" +
                "          end,0)\n" +
                "end\n" +
                "file.remove(\"" + uploadFileName + "\")\n";
        sendBuf = cmdPrep(cmd);
        int startPackets;
        if (packets == 1) { // small file
            startPackets = lastPacketSize;
        } else {
            startPackets = SendPacketSize;
        }
        sendBuf.add("_up(" + Integer.toString(packets) + "," + Integer.toString(startPackets) + "," + Integer.toString(lastPacketSize) + ")");
        log("Uploader: Starting...");
        startTime = System.currentTimeMillis();
        SendLock();
        rx_data = "";
        rcvBuf = "";
        try {
            serialPort.removeEventListener();
        } catch (Exception e) {
            log(e.toString());
        }
        try {
            serialPort.addEventListener(new PortFilesUploader(), Constants.PORT_MASK);
            log("Uploader: Add EventListener: Success.");
        } catch (SerialPortException e) {
            log("Uploader: Add EventListener Error. Canceled.");
            SendUnLock();
            return;
        }
        int delay = 10;
        j0();
        taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //log("send j="+Integer.toString(j));
                if (j < sendBuf.size()) {
                    send(addCR(sendBuf.get(j)), false);
                } else {
                    if ((j - sendBuf.size()) < sendPackets.size()) {
                        sendBytes(sendPackets.get(j - sendBuf.size()));
                    } else {
                        log("Sorry, bug found: j overflow");
                    }
                }
            }
        };
        timer = new Timer(delay, taskPerformer);
        timer.setRepeats(false);
        log("Uploader: Start");
        TerminalAdd("Uploading to ESP file " + uploadFileName + "...");
        timer.setInitialDelay(delay);
        WatchDog();
        timer.start();
    }

    private boolean LoadBinaryFile(File f) {
        boolean success = false;
        try {
            log("BinaryFileLoader: Try to load file " + f.getName() + " ...");
            fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);
            tx_byte = new byte[dis.available()];
            int size = dis.read(tx_byte);
            dis.close();
            fis.close();
            if (size == f.length()) {
                log("BinaryFileLoader: Load file " + f.getName() + ": Success, size:" + Long.toString(f.length()));
                success = true;
            } else {
                log("BinaryFileLoader: Load file " + f.getName() + ": Fail, file size:" + Long.toString(f.length()) + ", read:" + Integer.toString(size));
            }
        } catch (IOException e) {
            log("BinaryFileLoader: Load file " + f.getName() + ": FAIL.");
            log(e.toString());
            JOptionPane.showMessageDialog(null, "BinaryFileLoader: Error, file " + f.getName() + " can't be read!");
        }
        return success;
    }

    private int SplitDataToPackets() {
        sendPackets = new ArrayList<byte[]>();
        packets = tx_byte.length / SendPacketSize;
        log("1. packets = " + Integer.toString(packets));
        if ((tx_byte.length % SendPacketSize) > 0) packets++;
        log("2. packets = " + Integer.toString(packets));
        if (tx_byte.length < SendPacketSize) packets = 1;
        int remain = tx_byte.length;
        int lastPacketSize = -1;
        byte[] b;
        int pos = 0;
        for (int i = 0; i < packets; i++) {
            log("3. packet = " + Integer.toString(i));
            if (remain > SendPacketSize)
                b = new byte[SendPacketSize]; // default value is 200
            else {
                b = new byte[remain];
                lastPacketSize = remain;
            }
            System.arraycopy(tx_byte, pos, b, 0, b.length);
            sendPackets.add(b);
            log("BinaryFileLoader: Prepare next packet for send, len=" + Integer.toString(b.length));
            remain -= b.length;
            pos += b.length;
        }
        log("BinaryFileLoader: Total packets prepared=" + Integer.toString(sendPackets.size()));
        return lastPacketSize;
    }

    private void sendBytes(byte[] b) {
        if (!pOpen) {
            log("BytesSender: Serial port not open, operation FAILED.");
            return;
        }
        if (busyIcon) {
            Busy.setIcon(LED_BLUE);
            SnippetsBusy.setIcon(LED_BLUE);
        } else {
            Busy.setIcon(LED_RED);
            SnippetsBusy.setIcon(LED_RED);
        }
        busyIcon = !busyIcon;
        try {
            //log("BytesSender sending:" + b.toString().replace("\r\n", "<CR><LF>"));
            serialPort.writeBytes(b);
        } catch (SerialPortException ex) {
            log("BytesSender send FAIL:" + Arrays.toString(b).replace("\r\n", "<CR><LF>"));
        }
    }

    private void ViewFile(String fn) {
        String cmd = "_view=function()\n" +
                "local _line\n" +
                "if file.open(\"" + fn + "\",\"r\") then \n" +
                "    print(\"--FileView start\")\n" +
                "    repeat _line = file.readline() \n" +
                "        if (_line~=nil) then \n" +
                "            print(string.sub(_line,1,-2)) \n" +
                "        end \n" +
                "    until _line==nil\n" +
                "    file.close() \n" +
                "    print(\"--FileView done.\") \n" +
                "else\n" +
                "  print(\"\\r--FileView error: can't open file\")\n" +
                "end\n" +
                "end\n" +
                "_view()\n" +
                "_view=nil\n";
        LocalEcho = false;
        SendToESP(cmdPrep(cmd));
    }

    private class PortFilesReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String data = serialPort.readString(event.getEventValue());
                    rcvBuf = rcvBuf + data;
                    rx_data = rx_data + data;
                } catch (Exception e) {
                    log(e.toString());
                }
                if (rcvBuf.contains("> ")) {
                    try {
                        timeout.restart();
                    } catch (Exception e) {
                        log(e.toString());
                    }
                    rcvBuf = "";
                    if (j < sendBuf.size() - 1) {
                        if (!timer.isRunning()) {
                            inc_j();
                            timer.start();
                        }
                    } else { // send done
                        try {
                            timer.stop();
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, e.getMessage());
                        }
                    }
                }
                try {
                    if (rx_data.contains("~~~File list END~~~")) {
                        try {
                            timeout.stop();
                        } catch (Exception e) {
                            log(e.toString());
                        }
                        ProgressBar.setValue(100);
                        log("FileManager: File list found! Do parsing...");
                        try {
                            // parsing answer
                            int start = rx_data.indexOf("~~~File list START~~~");
                            rx_data = rx_data.substring(start + 23, rx_data.indexOf("~~~File list END~~~"));
                            //log(rx_data.replaceAll("\r?\n", "<CR+LF>\r\n"));
                            s = rx_data.split("\r?\n");
                            Arrays.sort(s);
//                            TerminalAdd("\r\n" + rx_data + "\r\n> ");
                            int usedSpace = 0;
                            TerminalAdd("\r\n----------------------------");
                            for (String subs : s) {
                                TerminalAdd("\r\n" + subs);
                                String[] parts = subs.split(":");
                                if (parts[0].trim().length() > 0) {
                                    int size = Integer.parseInt(parts[1].trim().split(" ")[0]);
                                    AddFileButton(parts[0].trim(), size);
                                    usedSpace += size;
                                    log("FileManager found file " + parts[0].trim());
                                }
                            }
                            if (FileAsButton.size() == 0) {
                                TerminalAdd("No files found.");
                                TerminalAdd("\r\n----------------------------\r\n> ");
                            } else {
                                TerminalAdd("\r\n----------------------------");
                                TerminalAdd("\r\nTotal file(s)   : " + Integer.toString(s.length));
                                TerminalAdd("\r\nTotal size      : " + Integer.toString(usedSpace) + " bytes\r\n");
                            }
                            FileManagerPane.invalidate();
                            FileManagerPane.doLayout();
                            FileManagerPane.repaint();
                            FileManagerPane.requestFocusInWindow();
                            log("FileManager: File list parsing done, found " + FileAsButton.size() + " file(s).");
                        } catch (Exception e) {
                            log(e.toString());
                        }
                        try {
                            serialPort.removeEventListener();
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, e.getMessage());
                        }
                        serialPort.addEventListener(new PortReader(), Constants.PORT_MASK);
                        SendUnLock();
                        FileSystemInfo();
                    }
                } catch (SerialPortException ex) {
                    log(ex.toString());
                }
            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                log("FileManager: Unknown serial port error received.");
            }
        }
    }

    private class PortFileDownloader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            String data;
            byte[] b;
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    b = serialPort.readBytes();
                    rx_byte = concatArray(rx_byte, b);
                    data = new String(b);
                    rcvBuf = rcvBuf + data;
                    rx_data = rx_data + data;
                    //TerminalAdd(data);
                } catch (SerialPortException e) {
                    log(e.toString());
                }
                if (rcvBuf.contains("> ")) {
                    try {
                        timeout.restart();
                    } catch (Exception e) {
                        log(e.toString());
                    }
                    rcvBuf = "";
                    if (j < sendBuf.size() - 1) {
                        if (!timer.isRunning()) {
                            inc_j();
                            timer.start();
                        }
                    } else { // send done
                        try {
                            timer.stop();
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, e.getMessage());
                        }
                    }
                }
                /*
                String l = data.replace("\r", "<CR>");
                l = l.replace("\n", "<LF>");
                l = l.replace("`", "<OK>");
                log("recv:" + l);
                */
                if ((rx_data.lastIndexOf("~~~DATA-END") >= 0) && (rx_data.lastIndexOf("~~~DATA-START") >= 0)) {
                    // we got full packet
                    rcvPackets.add(rx_data.split("~~~DATA-END")[0]); // store RAW data
                    rx_data = rx_data.substring(rx_data.indexOf("~~~DATA-END") + 11); // and remove it from buf
                    if (packets > 0) { // exclude div by zero
                        ProgressBar.setValue(rcvPackets.size() * 100 / packets);
                    }
                    //  ~~~DATA-START~~~buf~~~DATA-LENGTH~~~string.len(buf)~~~DATA-N~~~i~~~DATA-CRC~~~CheckSum~~~DATA-END
                    //0        1                  2                               3            4                     5
                    // split packet & check crc
                    int i = rcvPackets.size() - 1;
                    String[] part = rcvPackets.get(i).split("~~~DATA-CRC~~~");
                    PacketsCRC.add(Integer.parseInt(part[1]));
                    String left = part[0];
                    part = left.split("~~~DATA-N~~~");
                    PacketsNum.add(Integer.parseInt(part[1]));
                    left = part[0];
                    part = left.split("~~~DATA-LENGTH~~~");
                    PacketsSize.add(Integer.parseInt(part[1]));
                    left = part[0];
                    part = left.split("~~~DATA-START~~~");
                    PacketsData.add(part[1]);
                    int startData = FindPacketID(i + 1);
                    byte[] x;
                    if ((startData > 0) && (rx_byte.length >= (startData + PacketsSize.get(i)))) {
                        x = copyPartArray(rx_byte, startData, PacketsSize.get(i));
                        //log("Downloader: data from packet #" + Integer.toString(i+1) + " found in raw data");
                    } else {
                        x = new byte[0];
                        //log("Downloader: data packet #" + Integer.toString(i+1) + " not found in raw data.");
                        //log("raw date length " + rx_byte.length +
                        //    "\r\nstartData " + Integer.toString(startData) );
                    }
                    //rx_byte = new byte[0];
                    if (PacketsCRC.get(i) == CRC(x)) {
                        try {
                            timeout.restart();
                        } catch (Exception e) {
                            log(e.toString());
                        }
                        rcvFile = rcvFile + PacketsData.get(i);
                        PacketsByte = concatArray(PacketsByte, x);
                        log("Downloader: Receive packet: " + Integer.toString(PacketsNum.get(i)) + "/" + Integer.toString(packets) +
                                ", size:" + Integer.toString(PacketsSize.get(i)) +
                                ", CRC check: Success");
                    } else {
                        try {
                            timeout.stop();
                        } catch (Exception e) {
                            log(e.toString());
                        }
                        log("Downloader: Receive packets: " + Integer.toString(PacketsNum.get(i)) + "/" + Integer.toString(packets) +
                                ", size expected:" + Integer.toString(PacketsSize.get(i)) +
                                ", size received:" + Integer.toString(PacketsByte.length) +
                                "\r\n, CRC expected :" + Integer.toString(PacketsCRC.get(i)) +
                                "  CRC received :" + Integer.toString(CRC(x)));
                        log("Downloader: FAIL.");
                        PacketsCRC.clear();
                        PacketsNum.clear();
                        PacketsSize.clear();
                        PacketsData.clear();
                        rcvPackets.clear();
                        rcvFile = "";
                        PacketsByte = new byte[0];
                        FileDownloadFinisher(false);
                    }
                } else if ((rx_data.lastIndexOf("~~~DATA-TOTAL-END~~~") >= 0) && (PacketsNum.size() == packets)) {
                    try {
                        timeout.stop();
                    } catch (Exception e) {
                        log(e.toString());
                    }
                    ProgressBar.setValue(100);
                    log("Downloader: Receive final sequense. File download: Success");
                    //log(rx_data);
                    FileDownloadFinisher(true);
                }
            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                log("Downloader: Unknown serial port error received.");
                FileDownloadFinisher(false);
            }
        }
    }

    private class PortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String data = serialPort.readString(event.getEventValue());
                    if (portJustOpen) {
                        TerminalAdd("Got answer! AutoDetect firmware...\r\n");
                        portJustOpen = false;
                        openTimeout.stop();
                        UpdateButtons();
                        log("\r\nCommunication with MCU established.");
                        if (data.contains("\r\n>")) {
                            TerminalAdd("\r\nNodeMCU firmware detected.\r\n");
                            btnSend("=node.heap()");
                            LeftTab.setSelectedIndex(0);
                        } else if (data.contains("\r\nERR")) {
                            TerminalAdd("\r\nAT-based firmware detected.\r\n");
                            btnSend("AT+GMR");
                            LeftTab.setSelectedIndex(1);
                            RightExtraButtons.setVisible(false);
                            RightSnippetsPane.setVisible(false);
                            FileManagerPane.setVisible(false);
                        } else {
                            TerminalAdd("\r\nCan't autodetect firmware, because proper answer not received.\r\n");
                        }
                    } else {
                        if (LocalEcho) {
                            TerminalAdd(data);
                        } else {
                            if (data.contains("\r")) {
                                LocalEcho = true;
                                TerminalAdd(data.substring(data.indexOf("\r")));
                            }
                        }
                    }
                } catch (SerialPortException ex) {
                    log(ex.toString());
                }
            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                log("FileManager: Unknown serial port error received.");
            }
        }
    }

    private class PortExtraReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                String data = "";
                try {
                    data = serialPort.readString(event.getEventValue());
                } catch (SerialPortException ex) {
                    log(ex.toString());
                }
                data = data.replace(">> ", "");
                data = data.replace(">>", "");
                data = data.replace("\r\n> ", "");
                data = data.replace("\r\n\r\n", "\r\n");
                rcvBuf = rcvBuf + data;
                log("recv:" + data.replace("\r\n", "<CR><LF>"));
                TerminalAdd(data);
                if (rcvBuf.contains(sendBuf.get(j).trim())) {
                    // first, reset watchdog timer
                    try {
                        timeout.stop();
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, e.getMessage());
                    }
                    if (rcvBuf.contains("stdin:")) {
                        String msg[] = {"LUA interpreter error detected!", rcvBuf, "Click OK to continue."};
                        JOptionPane.showMessageDialog(null, msg);
                    }
                    rcvBuf = "";
                    if (j < sendBuf.size() - 1) {
                        if (!timer.isRunning()) {
                            inc_j();
                            int div = sendBuf.size() - 1;
                            if (div == 0) div = 1;
                            ProgressBar.setValue((j * 100) / div);
                            timer.start();
                        }
                    } else {  // send done
                        StopSend();
                    }
                }
                if (rcvBuf.contains("powered by Lua 5.")) {
                    StopSend();
                    String msg[] = {"ESP module reboot detected!", "Event: internal NodeMCU exception or power fail.", "Please, try again."};
                    JOptionPane.showMessageDialog(null, msg);
                }
            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                log("FileManager: Unknown serial port error received.");
            }
        }
    }

    private class PortTurboReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                String data = "";
                try {
                    data = serialPort.readString(event.getEventValue());
                } catch (SerialPortException ex) {
                    log(ex.toString());
                }
                rcvBuf = rcvBuf + data;
                String l = data.replace("\r", "<CR>");
                l = l.replace("\n", "<LF>");
                l = l.replace("`", "<OK>");
                log("recv:" + l);
                TerminalAdd(data);
                if (rcvBuf.contains("> ")) {
                    try {
                        timeout.stop(); // first, reset watchdog timer
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, e.getMessage());
                    }
                    rcvBuf = "";
                    if (j < sendBuf.size() - 1) {
                        if (!timer.isRunning()) {

                            inc_j();
                            int div = sendBuf.size() - 1;
                            if (div == 0) div = 1;
                            ProgressBar.setValue((j * 100) / div);
                            timer.start();
                        }
                    } else { // send done
                        StopSend();
                    }
                }
            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                log("FileManager: Unknown serial port error received.");
            }
        }
    }

    private class PortFilesUploader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            String data, crc_parsed;
            boolean gotProperAnswer = false;
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    data = serialPort.readString(event.getEventValue());
                    rcvBuf = rcvBuf + data;
                    rx_data = rx_data + data;
                    //log("rcv:"+data);
                } catch (Exception e) {
                    log(e.toString());
                }
                if (rcvBuf.contains("> ") && j < sendBuf.size()) {
                    //log("got intepreter answer, j="+Integer.toString(j));
                    rcvBuf = "";
                    gotProperAnswer = true;
                }
                if (rx_data.contains("~~~CRC-END~~~")) {
                    gotProperAnswer = true;
                    //log("Uploader: receiving packet checksum " + Integer.toString( j-sendBuf.size()  +1) + "/"
                    //                                           + Integer.toString( sendPackets.size() ) );
                    // parsing answer
                    int start = rx_data.indexOf("~~~CRC-START~~~");
                    //log("Before CRC parsing:"+rx_data);
                    crc_parsed = rx_data.substring(start + 15, rx_data.indexOf("~~~CRC-END~~~"));
                    rx_data = rx_data.substring(rx_data.indexOf("~~~CRC-END~~~") + 13);
                    //log("After  CRC parsing:"+crc_parsed);
                    int crc_received = Integer.parseInt(crc_parsed);
                    int crc_expected = CRC(sendPackets.get(j - sendBuf.size()));
                    if (crc_expected == crc_received) {
                        log("Uploader: receiving checksum " + Integer.toString(j - sendBuf.size() + 1) + "/"
                                + Integer.toString(sendPackets.size())
                                + " check: Success");
                        sendPacketsCRC.add(true);
                    } else {
                        log("Uploader: receiving checksum " + Integer.toString(j - sendBuf.size() + 1) + "/"
                                + Integer.toString(sendPackets.size())
                                + " check: Fail. Expected: " + Integer.toString(crc_expected)
                                + ", but received: " + Integer.toString(crc_received));
                        sendPacketsCRC.add(false);
                    }
                }
                if (gotProperAnswer) {
                    try {
                        timeout.restart();
                    } catch (Exception e) {
                        log(e.toString());
                    }
                    ProgressBar.setValue(j * 100 / (sendBuf.size() + sendPackets.size() - 1));
                    if (j < (sendBuf.size() + sendPackets.size())) {
                        if (!timer.isRunning()) {
                            inc_j();
                            timer.start();
                        }
                    } else {
                        try {
                            timer.stop();
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, e.getMessage());
                        }
                    }
                }
                if (j >= (sendBuf.size() + sendPackets.size())) {
                    LocalEcho = false;
                    send(addCR("_up=nil"), false);
                    try {
                        timer.stop();
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, e.getMessage());
                    }
                    try {
                        timeout.stop();
                    } catch (Exception e) {
                        log(e.toString());
                    }
                    //log("Uploader: send all data, finishing...");
                    boolean success = true;
                    for (Boolean aSendPacketsCRC : sendPacketsCRC) {
                        if (!aSendPacketsCRC) {
                            success = false;
                        }
                    }
                    if (success && (sendPacketsCRC.size() == sendPackets.size())) {
                        TerminalAdd("Success\r\n");
                        log("Uploader: Success");
                    } else {
                        TerminalAdd("Fail\r\n");
                        log("Uploader: Fail");
                    }
                    try {
                        serialPort.removeEventListener();
                    } catch (Exception e) {
                        log(e.toString());
                    }
                    try {
                        serialPort.addEventListener(new PortReader(), Constants.PORT_MASK);
                    } catch (Exception e) {
                        log(e.toString());
                    }
                    StopSend();
                }
            } else if (event.isCTS()) {
                UpdateLedCTS();
            } else if (event.isERR()) {
                log("FileManager: Unknown serial port error received.");
            }
        }
    }
}