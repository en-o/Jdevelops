package cn.jdevelops.authentication.sas.server.controller;

import cn.jdevelops.api.result.response.PageResult;
import cn.jdevelops.api.result.response.ResultPageVO;
import cn.jdevelops.authentication.sas.server.controller.dto.AuthorizationConsentPage;
import cn.jdevelops.authentication.sas.server.controller.dto.AuthorizationPage;
import cn.jdevelops.authentication.sas.server.controller.specification.AuthorizationConsentSpecification;
import cn.jdevelops.authentication.sas.server.controller.specification.AuthorizationSpecification;
import cn.jdevelops.authentication.sas.server.oauth.dao.Oauth2AuthorizationConsentDao;
import cn.jdevelops.authentication.sas.server.oauth.dao.Oauth2AuthorizationDao;
import cn.jdevelops.authentication.sas.server.oauth.entity.Oauth2Authorization;
import cn.jdevelops.authentication.sas.server.oauth.entity.Oauth2AuthorizationConsent;
import cn.jdevelops.authentication.sas.server.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 授权
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-01-28 02:02
 */
@RestController
@RequestMapping("/authorization")
@Slf4j
public class AuthorizationController {

    private final Oauth2AuthorizationConsentDao oauth2AuthorizationConsentDao;
    private final Oauth2AuthorizationDao oauth2AuthorizationDao;

    public AuthorizationController(Oauth2AuthorizationConsentDao oauth2AuthorizationConsentDao
            , Oauth2AuthorizationDao oauth2AuthorizationDao) {
        this.oauth2AuthorizationConsentDao = oauth2AuthorizationConsentDao;
        this.oauth2AuthorizationDao = oauth2AuthorizationDao;
    }

    /**
     * 查询授权信息
     * @param page 客户端id
     * @return String
     */
    @PostMapping("/page")
    public ResultPageVO<PageResult<Oauth2Authorization>> page(@RequestBody AuthorizationPage page){
        Specification<Oauth2Authorization> where = AuthorizationSpecification
                .loginNameLike(page.getLoginName())
                .and(AuthorizationSpecification.clientIdEq(page.getClientId()));
        Page<Oauth2Authorization> findAll = oauth2AuthorizationDao
                .findAll(where, PageUtil.sortPageOf(page.getSortPage()));
        PageResult<Oauth2Authorization> pages = PageUtil.toPage(findAll);
        return ResultPageVO.success(pages);
    }

    /**
     * 查询授权确认信息
     * @param page 客户端id
     * @return String
     */
    @PostMapping("/consent/page")
    public ResultPageVO<PageResult<Oauth2AuthorizationConsent>> page(@RequestBody AuthorizationConsentPage page){
        Specification<Oauth2AuthorizationConsent> where = AuthorizationConsentSpecification
                .loginNameLike(page.getLoginName())
                .and(AuthorizationConsentSpecification.clientIdEq(page.getClientId()));
        Page<Oauth2AuthorizationConsent> findAll = oauth2AuthorizationConsentDao
                .findAll(where, PageUtil.sortPageOf(page.getSortPage()));
        PageResult<Oauth2AuthorizationConsent> pages = PageUtil.toPage(findAll);
        return ResultPageVO.success(pages);
    }


}
