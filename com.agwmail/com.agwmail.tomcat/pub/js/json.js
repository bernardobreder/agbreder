var json = {
	encode : function(object) {
		var json = JSON.stringify(object);
		return base64.encode(json);
	},
	decode : function(encode) {
		var json = base64.decode(encode);
		return JSON.parse(json);
	}
}