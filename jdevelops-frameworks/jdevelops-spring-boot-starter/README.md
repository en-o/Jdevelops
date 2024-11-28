# 接口参数加密使用
## 后端
### 0. 工具类参考
[ApiSecurityUtilsTest.java](src/test/java/cn/tannn/jdevelops/frameworks/web/starter/apiSecurity/ApiSecurityUtilsTest.java)

### 1. 登录接口示例
```java
/**
 * 第一步生成静态的公私钥
 * <p> rsa常量 数据来源于（test/ApiEncrptTest.java）
 * <p> Map<String, String> rsaKey = RSAUtils.genKeyPair();
 */
public interface RSAKey {
    /**
     * 公钥
     */
    String publicKey = "公钥";
    /**
     * 私钥
     */
    String privateKey = "私钥";

}
```
```java
/**
 * 第二步前端：公钥获取
 */
@RestController
@RequestMapping("/encrypt")
@Slf4j
public class EncryptController {


    /**
     * 登录接口参数加密公钥
     * @return 公钥
     */
    @Operation(summary = "登录接口参数加密公钥")
    @ApiMapping(value = "/loginPublicKey",checkToken = false)
    public ResultVO<String> loginPublicKey(){
        return ResultVO.success(RSAKey.publicKey);
    }

}
```
```java
/**
 * 第三步登录接口
 */
@Operation(summary = "账户密码登录")
@ApiMapping(value = "/login", checkToken = false, method = RequestMethod.POST)
public ResultVO<LoginVO> login(@RequestBody @Valid ApiEncryptRes encryptData
        , HttpServletRequest request) throws Exception {
    // 解密
    String decrypt = ApiSecurityUtils.decrypt(encryptData.getAesKeyByRsa(), encryptData.getData(), RSAKey.privateKey);
    // 登录真正的参数
    LoginPassword login = JSON.to(LoginPassword.class, decrypt);
}
```

## 前端
> vue

0. 安装依赖
```shell
npm install jsencrypt crypto-js
```
1. 引入加解密工具类
[encryption.js](encryption.js)

2. 登录接口示例 `/api/user.js` 
```js
export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

// 登录接口参数加密公钥
export function loginPublicKey(params) {
  return request({
    url: '/encrypt/loginPublicKey',
    method: 'get',
    params
  })
}
```
3. `store/modules/user.js`
```js
import { login,  loginPublicKey } from '@/api/user'
const actions = {
    // user login
    login({ commit }, userInfo) {
        const { username, password } = userInfo
        return new Promise((resolve, reject) => {
            // 请求后端生成的编码
            loginPublicKey().then(res => {
                if (res.success) {
                    const publicKey = res.data
                    // 每次请求生成aeskey
                    const aesKey = get16RandomNum()
                    // 用登陆后后端生成并返回给前端的的RSA密钥对的公钥将AES16位密钥进行加密
                    const aesKeyByRsa = rsaEncrypt(aesKey, publicKey)
                    // 使用AES16位的密钥将请求报文加密（使用的是加密前的aes密钥）
                    const inputData = aesEncrypt(aesKey, JSON.stringify({ loginName: username.trim(), password: password }))
                    login({
                        aesKeyByRsa: aesKeyByRsa,
                        data: inputData,
                        frontPublicKey: publicKey
                    }).then(response => {
                        const { data } = response
                        commit('SET_TOKEN', data.token)
                        setToken(data.token)
                        resolve()
                    }).catch(error => {
                        reject(error)
                    })
                }
            })

            // login({ loginName: username.trim(), password: password }).then(response => {
            //   const { data } = response
            //   commit('SET_TOKEN', data.token)
            //   setToken(data.token)
            //   resolve()
            // }).catch(error => {
            //   reject(error)
            // })
        })
    },
}

```
4. page调用 handleLogin
```vue
methods: {
    handleLogin() {
      if (this.loginForm.username && this.loginForm.username !== '' && this.loginForm.password && this.loginForm.password !== '') {
        this.isLoading = true
        this.$store.dispatch('user/login', this.loginForm).then(() => {
          this.loginForm.password = ''
          this.$store.state.user.treaty = true
          // 登录后默认跳转首页/dashboard  记录路径this.redirect
          this.$router.push({ path: '/dashboard' || '/' }, () => {})
          this.isLoading = false
        }).catch(() => {
          this.isLoading = false
        })
      } else {
        this.$notify.error({
          title: '警告',
          message: `请输入${!this.loginForm.username ? `用户名` : !this.loginForm.password ? `密码` : `登录信息`}`,
          type: 'warning'
        })
      }
    }
}
```
