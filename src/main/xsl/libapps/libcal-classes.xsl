<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
	version="2.0">


	<xsl:template match="/classes">
		<html>
			<head>
				<title>classes</title>
			</head>
			<body>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>

	<xsl:template match="class">
		<div class="class">
			<div class="pure-g">
				<div class="pure-u-7-24">
					<div class="date">
						<xsl:apply-templates select="start" />
					</div>
					<div class="venue">
						<xsl:apply-templates select="location" />
					</div>
				</div>
				<div class="pure-u-17-24">
					<xsl:apply-templates select="title" />
					<div class="pure-g">
						<div class="pure-u-1-4">
							<xsl:apply-templates select="presenter" />
							<xsl:apply-templates select="remainingSeats" />
						</div>
						<div class="pure-u-3-4">
							<div>
								<xsl:apply-templates select="short_description" />
							</div>
							<xsl:if test="ends-with(./short_description, '...')">
								<div>
									<a>
										<xsl:attribute name="href" select="./url/public" />
										<xsl:text> Read More </xsl:text>
										<i class="fa-solid fa-arrow-right" />
									</a>
								</div>
							</xsl:if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="title">
		<h4>
			<a>
				<xsl:attribute name="href">
    	        <xsl:value-of select="../url/public" />
        	  	</xsl:attribute>
				<xsl:value-of select="." />
			</a>
		</h4>
	</xsl:template>


	<xsl:template match="start">
		<div class="month-day">
			<xsl:value-of select="month" />
			<xsl:text> </xsl:text>
			<xsl:value-of select="day" />
		</div>
		<div class="year">
			<xsl:value-of select="year" />
		</div>
		<div class="time">
			<xsl:if test="../allday = 'false' ">
				<xsl:value-of select="./hour" />
				<xsl:text> – </xsl:text>
				<xsl:value-of select="../end/hour" />
			</xsl:if>
			<xsl:if test="../allday = 'true' ">
				<xsl:text>All Day Event</xsl:text>
			</xsl:if>
		</div>
	</xsl:template>
	<xsl:template match="location">
		<xsl:if test=". != ''">
			<div class="location">
				<xsl:value-of select="." />
				<i class="fa-solid fa-location-dot fa-lg"></i>
			</div>
		</xsl:if>
	</xsl:template>
	<xsl:template match="presenter">
		<div class="instructor">
			Instructor(s):
			<div>
				<b>
					<xsl:value-of select="." />
				</b>
			</div>
		</div>
	</xsl:template>
	<xsl:template match="remainingSeats">
		<div class="register">
			<div>
				<a class="btn">
					<xsl:attribute name="href">
				          			 <xsl:apply-templates select="../url/public" />
				        	    </xsl:attribute>
					<span>Register</span>
				</a>
				<div class="remaining-seats">
					<xsl:if test="../seats = ''">
						<xsl:attribute name="style">
						visibility: hidden;
					</xsl:attribute>
					</xsl:if>
					Seats left:
					<xsl:value-of select="." />
				</div>
			</div>
		</div>
	</xsl:template>



</xsl:stylesheet>