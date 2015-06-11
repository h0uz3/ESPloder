package ESPlorer;

import jssc.SerialPort;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Created by h0uz3 on 04.06.15.
 */
public class Constants {
    public static final float TERMINAL_FONT_SIZE_DEFAULT = 16f;
    public static final float TERMINAL_FONT_SIZE_MAX = 40f;
    public static final float TERMINAL_FONT_SIZE_MIN = 5f;
    public static final float EDITOR_FONT_SIZE_DEFAULT = 13f;
    public static final float EDITOR_FONT_SIZE_MAX = 40f;
    public static final float EDITOR_FONT_SIZE_MIN = 8f;
    public static final float LOG_FONT_SIZE_DEFAULT = 10f;
    public static final float LOG_FONT_SIZE_MAX = 40f;
    public static final float LOG_FONT_SIZE_MIN = 5f;
    public static final String nodeRoot = "/com/esp8266.ru/ESPlorer/config";
    public static final String SERIAL_PORT = "serial_port";
    public static final String SERIAL_BAUD = "serial_baud";
    public static final String PATH = "path";
    public static final String FIRMWARE = "firmware";
    public static final String FILE_AUTO_SAVE_DISK = "file_auto_save_disk";
    public static final String FILE_AUTO_SAVE_ESP = "file_auto_save_esp";
    public static final String FILE_AUTO_RUN = "file_auto_run";
    public static final String COLOR_THEME = "color_theme";
    public static final String DELAY = "delay";
    public static final String TIMEOUT = "timeout";
    public static final String DUMB_MODE = "dumb_mode";
    public static final String TURBO_MODE = "turbo_mode";
    public static final String LINE_DELAY = "line_delay";
    public static final String TERMINAL_FONT_SIZE = "terminal_font_size";
    public static final String EDITOR_FONT_SIZE = "editor_font_size";
    public static final String LOG_FONT_SIZE = "log_font_size";
    public static final String LOG_MAX_SIZE = "log_max_size";
    public static final String TERMINAL_MAX_SIZE = "terminal_max_size";
    public static final String version = "v0.0.1_refacturing";
    public static final String[] EXTENSION_LUA = {"lua", "lc"};
    public static final String[] EXTENSION_PY = {"py"};
    public static final String AUTO_SCROLL = "auto_scroll";
    public static final String SHOW_LOG = "show_log";
    public static final String SHOW_TOOLBAR = "show_toolbar";
    public static final String SHOW_EXTRA_LEFT = "show_extra_left";
    public static final String SHOW_EXTRA_RIGHT = "show_extra_right";
    public static final String SHOW_SNIP_RIGHT = "show_snip_right";
    public static final String SHOW_FM_RIGHT = "show_fm_right";
    public static final String USE_CUSTOM_PORT = "use_custom_port";
    public static final String CUSTOM_PORT_NAME = "custom_port_name";
    public static final String LOG_DIV = "log_div";
    public static final String FM_DIV = "fm_div";
    public static final String PORT_RTS = "port_rts";
    public static final String PORT_DTR = "port_dtr";
    public static final String USE_EXT_EDITOR = "use_ext_editor";
    public static final String SHOW_DONATE = "show_donate";
    public static final String SHOW_EOL = "show_eol";
    public static final String WIN_X = "win_x";
    public static final String WIN_Y = "win_y";
    public static final String WIN_H = "win_h";
    public static final String WIN_W = "win_w";
    public static final String CONDENSED = "condensed";
    public static final String AUTODETECT = "autodetect_firmware";
    public static final int PORT_MASK = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS;
    public static final FileNameExtensionFilter FILTER_LUA = new FileNameExtensionFilter("LUA files (*.lua, *.lc)", Constants.EXTENSION_LUA);
    public static final FileNameExtensionFilter FILTER_PYTHON = new FileNameExtensionFilter("Python files (*.py)", Constants.EXTENSION_PY);


}
