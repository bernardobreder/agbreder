function ajaxTag(url, tag, func) {
	ajax(url, null, function(data) {
		$(tag).html(data);
		if (func != null) {
			func();
		}
	});
}

function ajax(url, json, func, error) {
	var loading = $('#loading').clone();
	$('body').append(loading)
	loading.show();
	$.ajax({
		url : url,
		data : json,
		timeout : 15 * 1000,
		success : function(data) {
			if (func != null) {
				try {
					func(data);
				} catch (err) {
					if (error != null) {
						error();
					}
				}
			}
		},
		error : function(data) {
			if (error != null) {
				error();
			}
		},
		complete : function() {
			loading.remove();
		}
	});
}
