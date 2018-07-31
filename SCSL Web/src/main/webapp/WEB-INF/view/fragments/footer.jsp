<!-- Modal -->
<div ></div>
<footer id="foot">

	<section class="bottomfooter">
		<div class="container-fluid">
			<div class="row">
				
					
					
					<!-- <div class="col-md-3 col-sm-12 col-xs-12 text-left footer-mob-view"
					>&copy;
					Copyright Aarogyasri 2017
				</div> -->
				<div class="col-md-8 col-sm-12 col-xs-12 footer-links">
				<a href="termsOfUse" >Terms of Use</a> | <a
						href="disclaimer" >Disclaimer</a> | <a href="privacyPolicy" >Privacy
						Policy</a> | <a href="siteMap" >Sitemap</a> &nbsp; &nbsp;
					
				</div>
				<div class="col-md-4 col-sm-12 col-xs-12 text-right footer-mob-view" style="padding-right: 40px;"
					><span class="footerfont">Powered by </span><a
						href="http://sdrc.co.in/" target="_blank" class="text_deco_none"><span style=" color: orange;"
						class="poweredbysdrc">SDRC</span></a>
				</div>

			</div>
		</div>
	</section>

</footer>

<script>
	$(document).ready(function(){
		
		if(location.pathname.split("/")[location.pathname.split("/").length-1] == ""){
			if(location.hash == "")
				$("li.home").addClass("active");
			else
				$("li.contact").addClass("active");
		}
		else{
			$("."+location.pathname.split("/")[location.pathname.split("/").length-1]).addClass("active");
		}
	})
</script>
