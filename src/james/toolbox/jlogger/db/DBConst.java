package james.toolbox.jlogger.db;

/**
 * 数据库常量。
 *
 * Created by Anchorer/duruixue on 2014/8/18.
 */
public interface DBConst {
    /**
     * 数据库整体配置
     */
    // 数据库名称
    public static final String DEFAULT_DB_NAME = "db_salesassist";
    // 数据库版本
    public static final int DEFAULT_DB_VERSION = 1;

    /**
     * 数据库表名
     */
    public static final String TABLE_LOG = "log";


    /**
     * 数据库字段集合
     */
    // 数据字段：
    public static final String[] COLUMNS_TABLE_LOG = new String[]{"time", "type", "message"};


}
