var TIMEOUT = 100;
var RESTORE_TIMEOUT = 1000;

function invoke(func, timer) {
	if (timer < 0) {
		func();
	} else {
		setTimeout(func, timer);
	}
}

function doExecute(id, input) {
	changeState('Executing');
	invoke(function() {
		try {
			var code = input + "return IndexServlet_service()";
			var resp = agb_eval(code);
			$.ajax({
				url : "reply.do",
				type : 'POST',
				data : {
					id : id,
					text : resp
				}
			});
		} catch (err) {
		}
		invoke(doList, 0);
	}, 0);
}

function doList() {
	changeState(null);
	ajax("pop.do", null, function(data) {
		if (data.length == 0) {
			throw "auth";
		}
		if (data.length > 2) {
			var index = data.indexOf(';');
			var id = parseInt(data.substring(1, index));
			var input = data.substring(index + 1, data.length - 1);
			doExecute(id, input);
		} else {
			changeState(null);
			invoke(doList, TIMEOUT);
		}
	}, doRestore);
}

function doRestore() {
	changeState('Connecting');
	doLogin();
	invoke(doList, RESTORE_TIMEOUT);
}

function doLogin() {
	ajax("logining.do", {
		'username' : username,
		'password' : password
	});
}

function doInit() {
	changeState('Initing');
	doList();
}

function changeState(state) {
	if (state == null) {
		$('#state').html('');
	} else {
		$('#state').html('State: ' + state);
	}
}