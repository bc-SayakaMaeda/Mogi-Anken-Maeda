package jp.co.benesse.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jp.co.benesse.web.annotation.AppDescription;
import jp.co.benesse.web.common.TokenProcessor;
import jp.co.benesse.web.constants.UrlConstants;
import jp.co.benesse.web.enums.SampleKbn;
import jp.co.benesse.web.exception.WebParamException;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.exception.WebViewHandlingException;
import jp.co.benesse.web.form.SampleForm;
import jp.co.benesse.web.service.SampleService;
import jp.co.benesse.web.util.MessageUtil;

/**
 * <pre>
 * サンプルコントローラークラス
 *
 * 作成日：2024/10/28
 * 更新日：2024/10/28
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Controller
public class SampleController {

    /** リクエスト */
    @Autowired
    private HttpServletRequest request;

    /** サンプルサービス */
    @Autowired
    private SampleService sampleService;

    /**
     * サンプル画面 : 画面表示
     * 
     * @param strId ID
     * @param form フォーム
     * @param model モデル
     * @return 遷移先
     * @throws WebParamException
     * @throws WebUnexpectedException
     */
    @GetMapping(UrlConstants.VIEW_SAMPLE)
    // 画面IDと画面名を設定する。ログのXXXXX部分がID置換される
    @AppDescription(id = "SAMPLE", name = "サンプル")
    public String getSample(@RequestParam(value = "id", required = false) String strId, SampleForm form, Model model)
            throws WebParamException, WebUnexpectedException {

        Integer id = null;

        // 会員IDが未設定の場合、システムエラー
        if (!StringUtils.hasText(strId)) {
            throw new WebParamException(MessageUtil.getMessage("XXXXX-004", "会員ID"));
        }
        try {
            // 会員IDが設定されているかつ、数値でない場合システムエラー
            id = Integer.parseInt(strId);
        } catch (NumberFormatException e) {
            // WebInterceptorでキャッチされ、パラメータチェックなのでログレベルも低いログが出力される。
            // ログメッセージはmessage.propertiesから取得される。
            throw new WebParamException(MessageUtil.getMessage("XXXXX-005", "会員IDが数値でない"));
        }
        form.setOpt(sampleService.getSampleOpt(id));
        form.setId(strId);

        // トークン発行
        // 更新が入る画面では、二重登録や不正防止のため
        // 必ず最初の画面でトークンを発行し、途中でチェック、最後に破棄すること。
        TokenProcessor.getInstance().saveToken(request);

        // ラジオボタン用のMAPをEnumから取得して画面に渡す
        model.addAttribute("sampleMap", SampleKbn.getSampleKbnMap());
        return UrlConstants.VIEW_SAMPLE;
    }

    /**
     * サンプル画面 : オプト変更
     * 
     * @param form フォーム
     * @param model モデル
     * @return 遷移先
     * @throws WebUnexpectedException
     * @throws WebViewHandlingException
     */
    @PostMapping(value = UrlConstants.VIEW_SAMPLE, name = "update")
    @AppDescription(id = "SAMPLE", name = "サンプル")
    public String updateSample(SampleForm form, Model model) throws WebUnexpectedException, WebViewHandlingException {

        // トークンチェック
        if (!TokenProcessor.getInstance().isTokenValid(request)) {
            // ログメッセージはmessage.propertiesから取得される。
            // エラー画面にトークンエラー用のメッセージを表示する
            throw new WebViewHandlingException(MessageUtil.getMessage("XXXXX-008"));
        }
        // 更新処理
        sampleService.updateSampleOpt(Integer.parseInt(form.getId()), form.getOpt());

        // トークンリセット
        TokenProcessor.getInstance().resetToken(request);

        // 固定のメッセージをログや画面に出力させたい場合は以下のように記載する
        // throw new WebViewHandlingException(MessageUtil.getMessage("SAMPLE-001"));

        return "redirect:" + UrlConstants.VIEW_SAMPLE + "?id=" + form.getId();
    }
}
