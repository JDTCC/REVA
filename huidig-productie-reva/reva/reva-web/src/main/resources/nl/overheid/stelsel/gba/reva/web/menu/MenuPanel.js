$(document).ready(function() {
	/*
	 * Show submenu's on delay
	 */
	var timer;
	$('.nav-main>ul>li>a').mouseover(function() {
		var obj = $(this);

		// show at once on hover on current menuitem
		if ($(this).hasClass('current'))
			var t = 100;
		else
			t = 250;

		// render submenu
		timer = setTimeout(function() {
			// reset...
			$('.nav-main .hover').removeClass('hover');
			$('.nav-main li a').siblings('ul').removeAttr('style');

			// ... and render
			obj.siblings('ul').addClass('hover');
			obj.addClass('hover');
		}, t);
	});

	// reset timers
	$('.nav-main a').mouseleave(function() {
		clearTimeout(timer);
	});

	// show default submenu once again
	$('.nav-main').mouseleave(function() {
		$('.nav-main .hover').removeClass('hover');
	});
});
