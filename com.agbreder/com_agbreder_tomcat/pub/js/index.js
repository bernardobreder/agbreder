var preinboxProcessing = 0;
var preinboxTimer = 1000;
var inboxFps = 1000;
var inboxTimer;

function onLoginPage() {
	ajaxTag('login.html', "#content");
}

function onInboxPage() {
	ajaxTag('inbox.html', '#center');
	inboxTimer = setTimeout('onInboxPage()', inboxFps);
}

function onAgentPage() {
	clearTimeout(inboxTimer);
	ajaxTag('agent.html', '#center', function() {
		ajaxTag('folder.html', '#agent-left-div');
	});
}

function onScriptPage() {
	clearTimeout(inboxTimer);
	ajaxTag('script.html', '#center');
}

function onMessagePage() {
	clearTimeout(inboxTimer);
	ajaxTag('message.html', '#center');
}

function doOpenMail(mailId) {
	clearTimeout(inboxTimer);
	ajaxTag('mail.html?id=' + mailId, '#center');
}

function doLogout() {
	clearTimeout(inboxTimer);
	ajax("logout.do", null, function() {
		window.open(".", "_self");
	});
}

function loadFolder(id) {
	ajaxTag('folder.html?id=' + id, '#agent-left-div');
	$("#agent-center-div").html("");
}

function loadFile(id) {
	ajaxTag('function.html?id=' + id, '#agent-center-div');
}

function compose_send_empty() {
	var to = $('#compose_to').val();
	var subject = $('#compose_subject').val();
	var msg = $('#compose_text').val();
	$("#compose_submit").attr("disabled", true);
	compose_send(to, subject, msg);
}

function compose_send(to, subject, text) {
	ajax("compose_send.do", {
		"to" : to,
		"subject" : subject,
		"text" : base64.encode(text),
	}, function(data) {
		onInboxPage();
	});
}

function preinbox() {
	ajax("preinbox_open.do", null, function(mails) {
		mails = json.decode64(mails);
		var count = preinboxProcessing = mails.length;
		if (preinboxProcessing == 0) {
			setTimeout('preinbox()', preinboxTimer);
		} else {
			for ( var n = 0; n < count; n++) {
				var mail = mails[n];
				preinbox_mail(mail.id, mail.code);
			}
		}
	});
}

function preinbox_mail(id, text) {
	var begin = text.indexOf('{{');
	if (begin >= 0) {
		var end = text.indexOf('}}', begin + 2);
		var code = text.substring(begin + 2, end);
		vm_exec_sec({
			'id' : id,
			'code' : code,
			'mail' : text,
			'vm' : null
		});
	} else {
		preinboxProcessing--;
		ajax("preinbox_close.do", {
			'id' : id,
			'text' : text
		}, function() {
			if (preinboxProcessing == 0) {
				setTimeout('preinbox()', preinboxTimer);
			}
		});
	}
}

function vm_exec_sec(context) {
	var contextJson = json.encode64(context);
	var code = "vm_exec_pri('" + contextJson + "');";
	setTimeout(code, 1);
}

function vm_exec_pri(context) {
	context = json.decode64(context);
	var code = context.code;
	context.vm = vm(code, context.vm);
	if (context.vm != null && context.vm.pc != null) {
		vm_exec_sec(context);
	} else {
		context.mail = context.mail.replace('{{' + context.code + '}}',
				context.vm);
		preinbox_mail(context.id, context.mail);
	}
}

function insertTab(o, e) {
	var kC = e.keyCode ? e.keyCode : e.charCode ? e.charCode : e.which;
	if (kC == 9 && !e.shiftKey && !e.ctrlKey && !e.altKey) {
		var oS = o.scrollTop;
		if (o.setSelectionRange) {
			var sS = o.selectionStart;
			var sE = o.selectionEnd;
			o.value = o.value.substring(0, sS) + "\t" + o.value.substr(sE);
			o.setSelectionRange(sS + 1, sS + 1);
			o.focus();
		} else if (o.createTextRange) {
			document.selection.createRange().text = "\t";
			e.returnValue = false;
		}
		o.scrollTop = oS;
		if (e.preventDefault) {
			e.preventDefault();
		}
		return false;
	}
	return true;
}

$(function() {
	onLoginPage();
});
