// preloader js
$(window).load(function(){
    $('.preloader').delay(1000) .fadeOut("slow"); // set duration in brackets    
});

// typed effect
$(function(){
    /* start typed element */
    //http://stackoverflow.com/questions/24874797/select-div-title-text-and-make-array-with-jquery
    //loop count changed to 1
    var subElementArray = $.map($('.sub-element'), function(el) { return $(el).text(); });    
    $(".element").typed({
        strings: subElementArray,
        typeSpeed: 30,
        contentType: 'html',
        showCursor: false,
        loop: true,
        loopCount: 1
    });
    /* end typed element */
    
    new WOW().init();
});
