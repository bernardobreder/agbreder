var username;
var password;

function logo() {
	$('#center').html($('#logo').html());
}

function forgot_password() {
	$('#center').html($('#forgot_password').html());
}

function create_account() {
	$('#center').html($('#create_account').html());
}

function create_account_submit() {
	$("create_account_button").attr("disabled", true);
	var user = $('#create_account_username').val();
	var email = $('#create_account_email').val();
	var pass = $('#create_account_password').val();
	$.ajax({
		url : "create_account.do",
		data : {
			'username' : user,
			'email' : email,
			'password' : pass
		},
		success : function(data) {
			if (data == 'null') {
				alert("Username or Email already Used. Try another!");
			} else {
				logo();
			}
		},
		complete : function() {
			$("create_account_button").removeAttr("disabled");
		}
	});
}

function logining() {
	username = $('#username').val();
	password = $('#password').val();
	ajax("logining.do", {
		'username' : username,
		'password' : password
	}, function(data) {
		data = json.decode64(data);
		if (data != null) {
			ajaxTag('app.html', "#content", doInit);
		} else {
			$('#password').val('');
		}
	});
}

function login() {
	if (event.keyCode == '13') {
		logining();
	}
}

$(function() {
	$("#password").keyup(login);
	$("#username").keyup(login);
});