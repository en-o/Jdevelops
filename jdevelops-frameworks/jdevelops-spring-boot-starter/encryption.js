import CryptoJS from 'crypto-js'
import * as forge from 'node-forge'

/**
 * 生成16位随机密钥 - 完全模仿 Java 的随机数生成规则
 */
export function get16RandomNum() {
    const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'
    let result = ''
    // 这个地方切记要选择16位，因为美国对密钥长度有限制，选择32位的话加解密会报错，需要根据jdk版本去修改相关jar包，有点恼火，选择16位就不用处理。
    for (let i = 0; i < 16; i++) {
        const randomIndex = Math.floor(Math.random() * chars.length)
        result += chars[randomIndex]
    }
    return result
}

/**
 * AES加密
 * @param {string} key - 16位密钥
 * @param {string} content - 待加密内容
 * @returns {string} Base64编码的加密结果
 */
export function aesEncrypt(key, content) {
    // 将密钥转换为 WordArray
    const keyWordArray = CryptoJS.enc.Utf8.parse(key)

    // 生成 16 字节随机 IV
    const iv = CryptoJS.lib.WordArray.random(16)

    // 加密
    const cipher = CryptoJS.AES.encrypt(
        CryptoJS.enc.Utf8.parse(content),
        keyWordArray,
        {
            iv: iv,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7
        }
    )

    // 将 IV 和密文组合
    const combinedBytes = CryptoJS.lib.WordArray.create()
        .concat(iv)
        .concat(cipher.ciphertext)

    // 返回 Base64 编码
    return CryptoJS.enc.Base64.stringify(combinedBytes)
}

/**
 * 使用 RSA 公钥加密
 * @param {string} data - 待加密数据
 * @param {string} publicKey - RSA公钥
 * @returns {string} Base64编码的加密结果
 */
export function rsaEncrypt(data, publicKeyPem) {

    console.log("进入加密方法"+data);
    data = encodeURIComponent(data);
    let pubKey2 = `-----BEGIN PUBLIC KEY-----\n${publicKeyPem}\n-----END PUBLIC KEY-----`;
    // 将PEM格式的公钥转换为forge的公钥对象
    const publicKey = forge.pki.publicKeyFromPem(pubKey2)

    // 使用OAEP填充方案加密
    const encrypted = publicKey.encrypt(data, 'RSA-OAEP', {
        md: forge.md.sha1.create(),
        mgf1: {
            md: forge.md.sha1.create()
        }
    })
    // 将加密结果转换为Base64
    return forge.util.encode64(forge.util.createBuffer(encrypted).getBytes())
}
