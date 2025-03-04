package jp.co.benesse.web;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

@TestExecutionListeners({
        ServletTestExecutionListener.class,
        DirtiesContextBeforeModesTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        ResetMocksTestExecutionListener.class,
        MockitoTestExecutionListener.class, })
@SpringBootTest
@TestPropertySource(locations = { "classpath:application-test.properties" })
public class BaseTest {

    /** テストメソッド名 */
    protected TestInfo testInfo = null;

    @Mock(lenient = true)
    protected Appender mockAppender;

    @Captor
    protected ArgumentCaptor<LogEvent> logCaptor;

    protected MockitoSession session;

    /**
     * <pre>
     * ユニットテストの事後処理
     * 各メソッド実行後に処理される
     * </pre>
     */
    @AfterEach
    public void tearDown() {
        // session.finishMocking();
    }

    /**
     * <pre>
     * ユニットテストの事前処理
     * 各メソッド実行前に処理される
     * </pre>
     *
     * @param testInfo テストメソッド名
     */
    @BeforeEach
    public void setup(TestInfo testInfo) {

        // session =
        // Mockito.mockitoSession().initMocks(this).strictness(Strictness.WARN).startMocking();

        this.testInfo = testInfo;
        Mockito.reset(mockAppender);
        // Appenderの名前を設定
        Mockito.when(mockAppender.getName()).thenReturn("MockAppender");
        // Appenderとして利用できる準備ができていることを設定（下2行）
        Mockito.when(mockAppender.isStarted()).thenReturn(true);
        Mockito.when(mockAppender.isStopped()).thenReturn(false);

        // ROOTロガーを取り出し、Appenderの設定を行う。
        // LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        // Configuration config = ctx.getConfiguration();

        // LoggerConfig loggerConfig =
        // config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);

        // loggerConfig.setLevel(Level.INFO);
        // loggerConfig.removeAppender("MockAppender");
        // loggerConfig.addAppender(mockAppender, Level.INFO, null);
        // ctx.updateLoggers();

    }

}