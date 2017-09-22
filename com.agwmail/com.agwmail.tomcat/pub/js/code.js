
vm('aaaaaaadaaaaaaaiaaaaaaacaaaaaaadaaaaaaajaaaaaaaaaaaaaaadaaaaaaacaaaaaaadaaaaaaaaaaaaaadfaaaaaacoaaaaaadaaaaaaaaaaaaaaaadaaaaaaacaaaaaaadaaaaaaaaaaaaaadcaaaaaacoaaaaaadcaaaaaaaaaaaaaaaeaaaaaaaeaaaaaaaaaaaaaaaeaaaaaaaeaaaaaaabaaaaaaaiaaaaaaacaaaaaaabaaaaaaab');
self.postMessage(1);
self.addEventListener('message', function(e) {
  self.postMessage("form Worker");
}, false);