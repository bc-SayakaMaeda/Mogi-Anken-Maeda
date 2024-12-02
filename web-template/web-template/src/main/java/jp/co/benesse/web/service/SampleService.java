package jp.co.benesse.web.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jp.co.benesse.web.entity.SampleEntity;
import jp.co.benesse.web.exception.WebParamException;
import jp.co.benesse.web.exception.WebUnexpectedException;
import jp.co.benesse.web.repository.SampleRepository;
import jp.co.benesse.web.util.MessageUtil;

/**
 * <pre>
 * サンプルサービス
 *
 * 作成日：2024/10/28
 * 更新日：2024/10/28
 * </pre>
 *
 * @author BC)yoda
 * @version 1.0
 */
@Service
public class SampleService {

    /** サンプルリポジトリ */
    @Autowired
    private SampleRepository sampleRepository;

    /**
     * オプト取得
     * 
     * @param id 会員ID
     * @return オプト
     * @throws WebUnexpectedException
     * @throws WebParamException
     */
    public String getSampleOpt(Integer id) throws WebUnexpectedException, WebParamException {

        String opt = sampleRepository.selectSampleOpt(id);
        if (!StringUtils.hasText(opt)) {
            throw new WebParamException(MessageUtil.getMessage("XXXXX-005", "会員ID"));
        }
        return opt;
    }

    /**
     * オプト変更
     * 
     * @param id 会員ID
     * @param opt オプト
     * @throws WebUnexpectedException
     */
    public void updateSampleOpt(Integer id, String opt) throws WebUnexpectedException {

        SampleEntity entity = new SampleEntity();
        entity.setMemId(id);
        entity.setOpt(opt);
        entity.setUpdateDt(LocalDateTime.now());
        sampleRepository.updateSampleOpt(entity);
    }

}
