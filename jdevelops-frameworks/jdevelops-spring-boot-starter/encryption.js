
import JSEncrypt from 'jsencrypt'
import CryptoJS from 'crypto-js'

/**
 * 加密
 * @param Str  16位随机码AES
 * @param afterPublicKey 公钥
 * @returns
 */
export function rsaEncrypt(Str, afterPublicKey) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(afterPublicKey) // 设置公钥
  return encryptor.encrypt(Str) // 对数据进行加密
}

// 解密
export function rsaDecrypt(Str, frontPrivateKey) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(frontPrivateKey) // 设置私钥
  return encryptor.decrypt(Str) // 对数据进行解密
}

/**
 * 报文加密
 * @param  aeskey 经过rsa加密的aeskey
 * @param  Str json字符串的数据报文
 * @returns
 */
export function aesEncrypt(aeskey, Str) {
  // 设置一个默认值，如果第二个参数为空采用默认值，不为空则采用新设置的密钥
  var key = CryptoJS.enc.Utf8.parse(aeskey)
  var srcs = CryptoJS.enc.Utf8.parse(Str)
  var encrypted = CryptoJS.AES.encrypt(srcs, key, {
    // 切记   需要和后端算法模式一致
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  })

  return encrypted.toString()
}

export function aesDecrypt(aeskey, Str) {
  var key = CryptoJS.enc.Utf8.parse(aeskey)
  var decrypt = CryptoJS.AES.decrypt(Str, key, {
    // 切记   需要和后端算法模式一致
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  })
  return CryptoJS.enc.Utf8.stringify(decrypt).toString()
}
/**
 * 获取16位随机码AES
 * @returns {string}
 */
export function get16RandomNum() {
  var chars = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
  ]
  var nums = ''
  // 这个地方切记要选择16位，因为美国对密钥长度有限制，选择32位的话加解密会报错，需要根据jdk版本去修改相关jar包，有点恼火，选择16位就不用处理。
  for (var i = 0; i < 16; i++) {
    var id = parseInt(Math.random() * 61)
    nums += chars[id]
  }
  return nums
}
// 获取rsa密钥对
export function getRsaKeys() {
  return new Promise((resolve, reject) => {
    window.crypto.subtle
        .generateKey(
            {
              name: 'RSA-OAEP',
              modulusLength: 2048, // can be 1024, 2048, or 4096
              publicExponent: new Uint8Array([0x01, 0x00, 0x01]),
              hash: { name: 'SHA-512' } // can be "SHA-1", "SHA-256", "SHA-384", or "SHA-512"
            },
            true, // whether the key is extractable (i.e. can be used in exportKey)
            ['encrypt', 'decrypt'] // must be ["encrypt", "decrypt"] or ["wrapKey", "unwrapKey"]
        )
        .then(function(key) {
          window.crypto.subtle
              .exportKey('pkcs8', key.privateKey)
              .then(function(keydata1) {
                window.crypto.subtle
                    .exportKey('spki', key.publicKey)
                    .then(function(keydata2) {
                      var privateKey = RSA2text(keydata1, 1)

                      var publicKey = RSA2text(keydata2)

                      resolve({ privateKey, publicKey })
                    })
                    .catch(function(err) {
                      reject(err)
                    })
              })
              .catch(function(err) {
                reject(err)
              })
        })
        .catch(function(err) {
          reject(err)
        })
  })
}
function RSA2text(buffer, isPrivate = 0) {
  var binary = ''
  var bytes = new Uint8Array(buffer)
  var len = bytes.byteLength
  for (var i = 0; i < len; i++) {
    binary += String.fromCharCode(bytes[i])
  }
  var base64 = window.btoa(binary)

  const text = base64.replace(/[^\x00-\xff]/g, '$&\x01').replace(/.{64}\x01?/g, '$&\n')

  return text
}
