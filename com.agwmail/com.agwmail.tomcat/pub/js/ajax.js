function ajaxTag(url, tag, func) {
	ajax(url, null, function(data) {
		$(tag).html(data);
		if (func != null) {
			func();
		}
	});
}

function ajax(url, json, func) {
	var loading = $('#loading').clone();
	$('body').append(loading)
	loading.show();
	$.ajax({
		url : url,
		data : json,
		dataType : "html",
		success : function(data) {
			if (func != null) {
				func(data);
			}
		},
		error : function() {
		},
		complete : function() {
			loading.remove();
		}
	});
}
