var json = {
	encode64 : function(object) {
		var data = $.toJSON(object);
		return base64.encode(data);
	},
	decode64 : function(encode) {
		var data = base64.decode(encode);
		return json.decode(data);
	},
	encode : function(object) {
		return $.toJSON(object);
	},
	decode : function(encode) {
		return $.evalJSON(encode);
	}
}