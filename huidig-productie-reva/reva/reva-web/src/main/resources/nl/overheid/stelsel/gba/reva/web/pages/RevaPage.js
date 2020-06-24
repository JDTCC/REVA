$(document).ready(function()
{
    /*
     * Datum picker
     */
    $.datepicker.setDefaults( $.datepicker.regional[ "" ] );
    $('.datepicker').datepicker(
        $.datepicker.regional[ "nl" ]
    );       
	/*
	 * Invulhulpen
	 */
    // focus op eerste input (als dat geen datepicker is)
    var firstElement = $('input[type="text"], input[type="checkbox"], input[type="radio"], select, textarea').first();
    if ( (' ' + firstElement[0].className).indexOf( 'datepicker' ) < 0 )  { 
    	firstElement.focus(); 
    }
	// vang reload af
	$(document).bind('keydown', function(e)
	{
		if (e.which === 116) // F5
		{
			console.log('blocked');
			return false;
		}
		if (e.which === 82 && e.ctrlKey) // cmd + R
		{
			console.log('blocked');
			return false;
		}
	});
	// Vang dubbelklik af (timeout 3 sec.)
	$('body').on('click', 'button, input[type="submit"], button, input[type="button"]', function()
	{
		var obj = $(this);
		if(obj.hasClass('busy')) event.preventDefault();
		obj.addClass('busy');

		window.setTimeout(function()
		{
			obj.removeClass('busy');
		}, 3000);
	});
});

/* Dutch (UTF-8) initialisation for the jQuery UI date picker plugin. */
/* Written by Mathias Bynens <http://mathiasbynens.be/> */
jQuery(function($){
	$.datepicker.regional.nl = {
		closeText: 'Sluiten',
		prevText: 'Vorige',
		nextText: 'Volgende',
		currentText: 'Vandaag',
		monthNames: ['januari', 'februari', 'maart', 'april', 'mei', 'juni', 'juli', 'augustus', 'september', 'oktober', 'november', 'december'],
		monthNamesShort: ['jan', 'feb', 'mrt', 'apr', 'mei', 'jun', 'jul', 'aug', 'sep', 'okt', 'nov', 'dec'],
		dayNames: ['zondag', 'maandag', 'dinsdag', 'woensdag', 'donderdag', 'vrijdag', 'zaterdag'],
		dayNamesShort: ['zon', 'maa', 'din', 'woe', 'don', 'vri', 'zat'],
		dayNamesMin: ['zo', 'ma', 'di', 'wo', 'do', 'vr', 'za'],
		weekHeader: 'Wk',
		dateFormat: 'yy-mm-dd',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: ''};
	$.datepicker.setDefaults($.datepicker.regional.nl);
});