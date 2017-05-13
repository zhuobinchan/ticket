function isLetter( s ){   
	var regu =  /^([a-zA-Z]|\d|_)*$/;
	var re = new RegExp(regu);
	if (re.test(s)) {
	   return true;
	} else {
	   return false;
	}
}
var hexcase = 0;
var b64pad = "";
function hex_pwd(s) {
	return rstr2hex(rstr_pwd(str2rstr_utf8(s)));
}
function bsf_pwd(s) {
	return rstr2bsf(rstr_pwd(str2rstr_utf8(s)));
}
function any_pwd(s, e) {
	return rstr2any(rstr_pwd(str2rstr_utf8(s)), e);
}
function hex_hmac_pwd(k, d) {
	return rstr2hex(rstr_hmac_pwd(str2rstr_utf8(k), str2rstr_utf8(d)));
}
function bsf_hmac_pwd(k, d) {
	return rstr2bsf(rstr_hmac_pwd(str2rstr_utf8(k), str2rstr_utf8(d)));
}
function any_hmac_pwd(k, d, e) {
	return rstr2any(rstr_hmac_pwd(str2rstr_utf8(k), str2rstr_utf8(d)), e);
}
function pwd_vm_test() {
	return hex_pwd("abc").toLowerCase() == "900150983cd24fb0d6963f7d28e17f72";
}

function rstr_pwd(s) {
	return binl2rstr(binl_pwd(rstr2binl(s), s.length * 8));
}
function rstr_hmac_pwd(key, data) {
	var bkey = rstr2binl(key);
	if (bkey.length > 16) {
		bkey = binl_pwd(bkey, key.length * 8);
	}
	var ipad = Array(16), opad = Array(16);
	for (var i = 0; i < 16; i++) {
		ipad[i] = bkey[i] ^ 909522486;
		opad[i] = bkey[i] ^ 1549556828;
	}
	var hash = binl_pwd(ipad.concat(rstr2binl(data)), 512 + data.length * 8);
	return binl2rstr(binl_pwd(opad.concat(hash), 512 + 128));
}
function rstr2hex(input) {
	try {
		hexcase;
	}
	catch (e) {
		hexcase = 0;
	}
	var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
	var output = "";
	var x;
	for (var i = 0; i < input.length; i++) {
		x = input.charCodeAt(i);
		output += hex_tab.charAt((x >>> 4) & 15) + hex_tab.charAt(x & 15);
	}
	return output;
}
function rstr2bsf(input) {
	try {
		b64pad;
	}
	catch (e) {
		b64pad = "";
	}
	var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	var output = "";
	var len = input.length;
	for (var i = 0; i < len; i += 3) {
		var triplet = (input.charCodeAt(i) << 16) | (i + 1 < len ? input.charCodeAt(i + 1) << 8 : 0) | (i + 2 < len ? input.charCodeAt(i + 2) : 0);
		for (var j = 0; j < 4; j++) {
			if (i * 8 + j * 6 > input.length * 8) {
				output += b64pad;
			} else {
				output += tab.charAt((triplet >>> 6 * (3 - j)) & 63);
			}
		}
	}
	return output;
}
function rstr2any(input, encoding) {
	var divisor = encoding.length;
	var i, j, q, x, quotient;
	var dividend = Array(Math.ceil(input.length / 2));
	for (i = 0; i < dividend.length; i++) {
		dividend[i] = (input.charCodeAt(i * 2) << 8) | input.charCodeAt(i * 2 + 1);
	}
	var full_length = Math.ceil(input.length * 8 / (Math.log(encoding.length) / Math.log(2)));
	var remainders = Array(full_length);
	for (j = 0; j < full_length; j++) {
		quotient = Array();
		x = 0;
		for (i = 0; i < dividend.length; i++) {
			x = (x << 16) + dividend[i];
			q = Math.floor(x / divisor);
			x -= q * divisor;
			if (quotient.length > 0 || q > 0) {
				quotient[quotient.length] = q;
			}
		}
		remainders[j] = x;
		dividend = quotient;
	}
	var output = "";
	for (i = remainders.length - 1; i >= 0; i--) {
		output += encoding.charAt(remainders[i]);
	}
	return output;
}
function str2rstr_utf8(input) {
	var output = "";
	var i = -1;
	var x, y;
	while (++i < input.length) {
		x = input.charCodeAt(i);
		y = i + 1 < input.length ? input.charCodeAt(i + 1) : 0;
		if (55296 <= x && x <= 56319 && 56320 <= y && y <= 57343) {
			x = 65536 + ((x & 1023) << 10) + (y & 1023);
			i++;
		}
		if (x <= 127) {
			output += String.fromCharCode(x);
		} else {
			if (x <= 2047) {
				output += String.fromCharCode(192 | ((x >>> 6) & 31), 128 | (x & 63));
			} else {
				if (x <= 65535) {
					output += String.fromCharCode(224 | ((x >>> 12) & 15), 128 | ((x >>> 6) & 63), 128 | (x & 63));
				} else {
					if (x <= 2097151) {
						output += String.fromCharCode(240 | ((x >>> 18) & 7), 128 | ((x >>> 12) & 63), 128 | ((x >>> 6) & 63), 128 | (x & 63));
					}
				}
			}
		}
	}
	return output;
}
function str2rstr_utf16le(input) {
	var output = "";
	for (var i = 0; i < input.length; i++) {
		output += String.fromCharCode(input.charCodeAt(i) & 255, (input.charCodeAt(i) >>> 8) & 255);
	}
	return output;
}
function str2rstr_utf16be(input) {
	var output = "";
	for (var i = 0; i < input.length; i++) {
		output += String.fromCharCode((input.charCodeAt(i) >>> 8) & 255, input.charCodeAt(i) & 255);
	}
	return output;
}
function rstr2binl(input) {
	var output = Array(input.length >> 2);
	for (var i = 0; i < output.length; i++) {
		output[i] = 0;
	}
	for (var i = 0; i < input.length * 8; i += 8) {
		output[i >> 5] |= (input.charCodeAt(i / 8) & 255) << (i % 32);
	}
	return output;
}
function binl2rstr(input) {
	var output = "";
	for (var i = 0; i < input.length * 32; i += 8) {
		output += String.fromCharCode((input[i >> 5] >>> (i % 32)) & 255);
	}
	return output;
}
function binl_pwd(x, len) {
	x[len >> 5] |= 128 << ((len) % 32);
	x[(((len + 64) >>> 9) << 4) + 14] = len;
	var a = 1732584193;
	var b = -271733879;
	var c = -1732584194;
	var d = 271733878;
	for (var i = 0; i < x.length; i += 16) {
		var olda = a;
		var oldb = b;
		var oldc = c;
		var oldd = d;
		a = pwd_ff(a, b, c, d, x[i + 0], 7, -680876936);
		d = pwd_ff(d, a, b, c, x[i + 1], 12, -389564586);
		c = pwd_ff(c, d, a, b, x[i + 2], 17, 606105819);
		b = pwd_ff(b, c, d, a, x[i + 3], 22, -1044525330);
		a = pwd_ff(a, b, c, d, x[i + 4], 7, -176418897);
		d = pwd_ff(d, a, b, c, x[i + 5], 12, 1200080426);
		c = pwd_ff(c, d, a, b, x[i + 6], 17, -1473231341);
		b = pwd_ff(b, c, d, a, x[i + 7], 22, -45705983);
		a = pwd_ff(a, b, c, d, x[i + 8], 7, 1770035416);
		d = pwd_ff(d, a, b, c, x[i + 9], 12, -1958414417);
		c = pwd_ff(c, d, a, b, x[i + 10], 17, -42063);
		b = pwd_ff(b, c, d, a, x[i + 11], 22, -1990404162);
		a = pwd_ff(a, b, c, d, x[i + 12], 7, 1804603682);
		d = pwd_ff(d, a, b, c, x[i + 13], 12, -40341101);
		c = pwd_ff(c, d, a, b, x[i + 14], 17, -1502002290);
		b = pwd_ff(b, c, d, a, x[i + 15], 22, 1236535329);
		a = pwd_gg(a, b, c, d, x[i + 1], 5, -165796510);
		d = pwd_gg(d, a, b, c, x[i + 6], 9, -1069501632);
		c = pwd_gg(c, d, a, b, x[i + 11], 14, 643717713);
		b = pwd_gg(b, c, d, a, x[i + 0], 20, -373897302);
		a = pwd_gg(a, b, c, d, x[i + 5], 5, -701558691);
		d = pwd_gg(d, a, b, c, x[i + 10], 9, 38016083);
		c = pwd_gg(c, d, a, b, x[i + 15], 14, -660478335);
		b = pwd_gg(b, c, d, a, x[i + 4], 20, -405537848);
		a = pwd_gg(a, b, c, d, x[i + 9], 5, 568446438);
		d = pwd_gg(d, a, b, c, x[i + 14], 9, -1019803690);
		c = pwd_gg(c, d, a, b, x[i + 3], 14, -187363961);
		b = pwd_gg(b, c, d, a, x[i + 8], 20, 1163531501);
		a = pwd_gg(a, b, c, d, x[i + 13], 5, -1444681467);
		d = pwd_gg(d, a, b, c, x[i + 2], 9, -51403784);
		c = pwd_gg(c, d, a, b, x[i + 7], 14, 1735328473);
		b = pwd_gg(b, c, d, a, x[i + 12], 20, -1926607734);
		a = pwd_hh(a, b, c, d, x[i + 5], 4, -378558);
		d = pwd_hh(d, a, b, c, x[i + 8], 11, -2022574463);
		c = pwd_hh(c, d, a, b, x[i + 11], 16, 1839030562);
		b = pwd_hh(b, c, d, a, x[i + 14], 23, -35309556);
		a = pwd_hh(a, b, c, d, x[i + 1], 4, -1530992060);
		d = pwd_hh(d, a, b, c, x[i + 4], 11, 1272893353);
		c = pwd_hh(c, d, a, b, x[i + 7], 16, -155497632);
		b = pwd_hh(b, c, d, a, x[i + 10], 23, -1094730640);
		a = pwd_hh(a, b, c, d, x[i + 13], 4, 681279174);
		d = pwd_hh(d, a, b, c, x[i + 0], 11, -358537222);
		c = pwd_hh(c, d, a, b, x[i + 3], 16, -722521979);
		b = pwd_hh(b, c, d, a, x[i + 6], 23, 76029189);
		a = pwd_hh(a, b, c, d, x[i + 9], 4, -640364487);
		d = pwd_hh(d, a, b, c, x[i + 12], 11, -421815835);
		c = pwd_hh(c, d, a, b, x[i + 15], 16, 530742520);
		b = pwd_hh(b, c, d, a, x[i + 2], 23, -995338651);
		a = pwd_ii(a, b, c, d, x[i + 0], 6, -198630844);
		d = pwd_ii(d, a, b, c, x[i + 7], 10, 1126891415);
		c = pwd_ii(c, d, a, b, x[i + 14], 15, -1416354905);
		b = pwd_ii(b, c, d, a, x[i + 5], 21, -57434055);
		a = pwd_ii(a, b, c, d, x[i + 12], 6, 1700485571);
		d = pwd_ii(d, a, b, c, x[i + 3], 10, -1894986606);
		c = pwd_ii(c, d, a, b, x[i + 10], 15, -1051523);
		b = pwd_ii(b, c, d, a, x[i + 1], 21, -2054922799);
		a = pwd_ii(a, b, c, d, x[i + 8], 6, 1873313359);
		d = pwd_ii(d, a, b, c, x[i + 15], 10, -30611744);
		c = pwd_ii(c, d, a, b, x[i + 6], 15, -1560198380);
		b = pwd_ii(b, c, d, a, x[i + 13], 21, 1309151649);
		a = pwd_ii(a, b, c, d, x[i + 4], 6, -145523070);
		d = pwd_ii(d, a, b, c, x[i + 11], 10, -1120210379);
		c = pwd_ii(c, d, a, b, x[i + 2], 15, 718787259);
		b = pwd_ii(b, c, d, a, x[i + 9], 21, -343485551);
		a = safe_add(a, olda);
		b = safe_add(b, oldb);
		c = safe_add(c, oldc);
		d = safe_add(d, oldd);
	}
	return Array(a, b, c, d);
}
function pwd_cmn(q, a, b, x, s, t) {
	return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b);
}
function pwd_ff(a, b, c, d, x, s, t) {
	return pwd_cmn((b & c) | ((~b) & d), a, b, x, s, t);
}
function pwd_gg(a, b, c, d, x, s, t) {
	return pwd_cmn((b & d) | (c & (~d)), a, b, x, s, t);
}
function pwd_hh(a, b, c, d, x, s, t) {
	return pwd_cmn(b ^ c ^ d, a, b, x, s, t);
}
function pwd_ii(a, b, c, d, x, s, t) {
	return pwd_cmn(c ^ (b | (~d)), a, b, x, s, t);
}
function safe_add(x, y) {
	var lsw = (x & 65535) + (y & 65535);
	var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
	return (msw << 16) | (lsw & 65535);
}
function bit_rol(num, cnt) {
	return (num << cnt) | (num >>> (32 - cnt));
}
function encrypt(text) {
	var s = hex_pwd(text);
	return s.substr(8, 16);
}

