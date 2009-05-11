<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:output encoding="UTF-8" indent="no" media-type="text/html" method="html">
<!-- generated by dexter from `profile.html'  -->
</xsl:output><xsl:template match="/"><xsl:call-template name="profile-html-resume"/></xsl:template><xsl:template name="profile-html-resume"><xsl:element name="div"><xsl:attribute name="id"><xsl:text>resume</xsl:text></xsl:attribute><xsl:attribute name="name">resume</xsl:attribute><xsl:text>
						</xsl:text><xsl:element name="div"><xsl:attribute name="id"><xsl:text>title</xsl:text></xsl:attribute><xsl:element name="h2"><xsl:attribute name="align">center</xsl:attribute><xsl:attribute name="class">title</xsl:attribute><xsl:value-of select=".//Resume/rsName"/></xsl:element></xsl:element><xsl:text>
						</xsl:text><xsl:choose><xsl:when test="result/ResumeVars/privateFlag"><xsl:element name="div"><xsl:element name="p"><xsl:text>This resume is not public yet</xsl:text></xsl:element></xsl:element></xsl:when><xsl:when test="result/ResumeVars/protectedFlag"><xsl:element name="div"><xsl:element name="p"><xsl:text>This resume can only be seen by friends</xsl:text></xsl:element></xsl:element></xsl:when><xsl:when test="result/ResumeVars/publicFlag"><xsl:element name="div"><xsl:element name="p"><xsl:text>This resume is public</xsl:text></xsl:element></xsl:element></xsl:when><xsl:otherwise><xsl:element name="div"><xsl:element name="p"><xsl:text>Unknown status</xsl:text></xsl:element></xsl:element></xsl:otherwise></xsl:choose><xsl:text>
	
					
						</xsl:text><!-- Description --><xsl:text>
						</xsl:text><xsl:element name="div"><xsl:attribute name="id"><xsl:text>resumeDescriptionContainer</xsl:text></xsl:attribute><xsl:text>
							</xsl:text><xsl:element name="div"><xsl:attribute name="class">clearboth</xsl:attribute></xsl:element><xsl:text>
							</xsl:text><xsl:element name="div"><xsl:attribute name="class">title</xsl:attribute><xsl:value-of select="result/Resume/rsUserDesc"/></xsl:element><xsl:text>
							</xsl:text><xsl:choose><xsl:when test="result/Resume/rsSummaryOfQual"><xsl:element name="div"><xsl:text>
								</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeSectionTitle</xsl:attribute><xsl:text>Summary of Qualifications</xsl:text></xsl:element><xsl:text>
								</xsl:text><xsl:element name="div"><xsl:attribute name="class">subtitle</xsl:attribute><xsl:value-of select="result/Resume/rsSummaryOfQual"/></xsl:element><xsl:text>
							</xsl:text></xsl:element></xsl:when><xsl:otherwise><xsl:element name="div"/></xsl:otherwise></xsl:choose><xsl:text>
						</xsl:text></xsl:element><xsl:text>
						</xsl:text><!-- Description --><xsl:text>
						</xsl:text><!-- skills --><xsl:text>
						</xsl:text><xsl:element name="div"><xsl:attribute name="id"><xsl:text>resumeSkillsContainer</xsl:text></xsl:attribute><xsl:text>
							</xsl:text><!-- core competencies --><xsl:text>
							</xsl:text><xsl:element name="div"><xsl:attribute name="id"><xsl:text>resumeCoreCompetencies</xsl:text></xsl:attribute><xsl:text>
								</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeSectionTitle</xsl:attribute><xsl:text>Core Competencies</xsl:text></xsl:element><xsl:text>
								</xsl:text><xsl:element name="div"><xsl:attribute name="id"><xsl:text>resumeCoreCompet</xsl:text></xsl:attribute><xsl:text>
									</xsl:text><xsl:for-each select="result/Skills/*"><xsl:variable name="DexterDepthLevel1"><xsl:value-of select="position()"/></xsl:variable><xsl:element name="div"><xsl:text>
										</xsl:text><xsl:if test="displayTopic"><xsl:element name="div"><xsl:attribute name="class">CoreCompet floatleft</xsl:attribute><xsl:value-of select="topic"/></xsl:element></xsl:if><xsl:text>
									</xsl:text></xsl:element></xsl:for-each><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><xsl:text>
								</xsl:text></xsl:element><xsl:text>
								</xsl:text><xsl:element name="div"><xsl:attribute name="class">clearboth</xsl:attribute></xsl:element><xsl:text>
							</xsl:text></xsl:element><xsl:text>
							</xsl:text><!-- core competencies --><xsl:text>
							</xsl:text><!-- Technical Expertise --><xsl:text>
							</xsl:text><xsl:element name="div"><xsl:attribute name="id"><xsl:text>resumeTechnicalExpertise</xsl:text></xsl:attribute><xsl:text>
								</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeSectionTitle</xsl:attribute><xsl:text>Technical Expertise</xsl:text></xsl:element><xsl:text>
								</xsl:text><xsl:element name="div"><xsl:attribute name="class">floatleft</xsl:attribute><xsl:attribute name="id"><xsl:text>resumeskills</xsl:text></xsl:attribute><xsl:text>
									</xsl:text><xsl:element name="div"><xsl:attribute name="id"><xsl:text>techExpertiseBlock</xsl:text></xsl:attribute><xsl:text>
										</xsl:text><!--skill Catagory --><xsl:text>
										</xsl:text><xsl:for-each select="result/Skills/*"><xsl:variable name="DexterDepthLevel1"><xsl:value-of select="position()"/></xsl:variable><xsl:element name="div"><xsl:text>
											</xsl:text><xsl:if test="breakdown"><xsl:element name="div"><xsl:text>
												</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeskillCat floatleft</xsl:attribute><xsl:value-of select="topic"/></xsl:element><xsl:text>
													</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeskills</xsl:attribute><xsl:text>
														</xsl:text><xsl:for-each select="breakdown/*"><xsl:variable name="DexterDepthLevel2"><xsl:value-of select="position()"/></xsl:variable><xsl:element name="div"><xsl:attribute name="class">userskills floatleft</xsl:attribute><xsl:value-of select="."/></xsl:element></xsl:for-each><xsl:text>
														</xsl:text><xsl:text>
														</xsl:text><xsl:text>
														</xsl:text><xsl:text>
														</xsl:text><xsl:text>
														</xsl:text><xsl:text>
														</xsl:text><xsl:text>
														</xsl:text><xsl:text>
														</xsl:text><xsl:text>
														</xsl:text><xsl:text>
														</xsl:text><xsl:text>
														</xsl:text><xsl:text>
														</xsl:text><xsl:text>
														</xsl:text><xsl:text>
													</xsl:text></xsl:element><xsl:text>
													</xsl:text><xsl:element name="div"><xsl:attribute name="class">clearboth</xsl:attribute></xsl:element><xsl:text>
												</xsl:text></xsl:element></xsl:if><xsl:text>
											</xsl:text></xsl:element></xsl:for-each><xsl:text>	
										</xsl:text><!--skill Catagory --><xsl:text>
										</xsl:text><!--skill Catagory --><xsl:text>
											</xsl:text><xsl:text>	
										</xsl:text><!--skill Catagory --><xsl:text>
										</xsl:text><!--skill Catagory --><xsl:text>
											</xsl:text><xsl:text>	
										</xsl:text><!--skill Catagory --><xsl:text>		
									</xsl:text></xsl:element><xsl:text>
								</xsl:text></xsl:element><xsl:text>
							</xsl:text></xsl:element><xsl:text>
							</xsl:text><!-- Technical Expertise --><xsl:text>
							</xsl:text><xsl:element name="div"><xsl:attribute name="class">clearboth</xsl:attribute></xsl:element><xsl:text>
						</xsl:text></xsl:element><xsl:text>
						</xsl:text><!-- skills --><xsl:text>
						</xsl:text><!-- Work History --><xsl:text>
						</xsl:text><xsl:element name="div"><xsl:attribute name="id"><xsl:text>resumeWorkHistory</xsl:text></xsl:attribute><xsl:text>
							</xsl:text><xsl:for-each select="result/EmploymentHistory/*"><xsl:variable name="DexterDepthLevel1"><xsl:value-of select="position()"/></xsl:variable><xsl:element name="div"><xsl:text>
								</xsl:text><!-- experience Type --><xsl:text>
								</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeSectionTitle</xsl:attribute><xsl:value-of select="experienceType"/><xsl:text> Experience</xsl:text></xsl:element><xsl:text>
								</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeProfExperience</xsl:attribute><xsl:text>
									</xsl:text><!-- job block --><xsl:text>
									</xsl:text><xsl:for-each select="jobs/*"><xsl:variable name="DexterDepthLevel2"><xsl:value-of select="position()"/></xsl:variable><xsl:element name="div"><xsl:text>
										</xsl:text><xsl:element name="div"><xsl:text>
											</xsl:text><xsl:element name="div"><xsl:attribute name="class">company floatleft</xsl:attribute><xsl:value-of select="company"/></xsl:element><xsl:text>
											</xsl:text><xsl:element name="div"><xsl:attribute name="class">location floatleft</xsl:attribute><xsl:value-of select="location"/></xsl:element><xsl:text>
											</xsl:text><xsl:element name="div"><xsl:attribute name="class">clearboth</xsl:attribute></xsl:element><xsl:text>
										</xsl:text></xsl:element><xsl:text>
										</xsl:text><xsl:element name="div"><xsl:text>
											</xsl:text><xsl:element name="div"><xsl:attribute name="class">jobTitle floatleft</xsl:attribute><xsl:value-of select="jobTitle"/></xsl:element><xsl:text>
											</xsl:text><xsl:element name="div"><xsl:attribute name="class">duration floatleft</xsl:attribute><xsl:value-of select="duration"/></xsl:element><xsl:text>
											</xsl:text><xsl:element name="div"><xsl:attribute name="class">clearboth</xsl:attribute></xsl:element><xsl:text>
										</xsl:text></xsl:element><xsl:text>
										</xsl:text><xsl:if test="tasks"><xsl:element name="div"><xsl:text>
											</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeJobTasks</xsl:attribute><xsl:text>
												</xsl:text><xsl:element name="ul"><xsl:text>
													</xsl:text><xsl:for-each select="tasks/*"><xsl:variable name="DexterDepthLevel3"><xsl:value-of select="position()"/></xsl:variable><xsl:element name="li"><xsl:value-of select="desc"/></xsl:element></xsl:for-each><xsl:text>
													</xsl:text><xsl:text>
													</xsl:text><xsl:text>
													</xsl:text><xsl:text>
													</xsl:text><xsl:text>
												</xsl:text></xsl:element><xsl:text>
											</xsl:text></xsl:element><xsl:text>
										</xsl:text></xsl:element></xsl:if><xsl:text>
										</xsl:text><xsl:if test="achievements"><xsl:element name="div"><xsl:text>
											</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeSectionSubTitle</xsl:attribute><xsl:text>Key Achievements</xsl:text></xsl:element><xsl:text>
											</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeJobTasks</xsl:attribute><xsl:text>
												</xsl:text><xsl:element name="ul"><xsl:text>
													</xsl:text><xsl:for-each select="achievements/*"><xsl:variable name="DexterDepthLevel3"><xsl:value-of select="position()"/></xsl:variable><xsl:element name="li"><xsl:value-of select="desc"/></xsl:element></xsl:for-each><xsl:text>
													</xsl:text><xsl:text>
													</xsl:text><xsl:text>
													</xsl:text><xsl:text>
													</xsl:text><xsl:text>
												</xsl:text></xsl:element><xsl:text>
											</xsl:text></xsl:element><xsl:text>
										</xsl:text></xsl:element></xsl:if><xsl:text>
									</xsl:text></xsl:element></xsl:for-each><xsl:text>
									</xsl:text><!-- job block --><xsl:text>
									</xsl:text><!-- job block --><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><!-- job block --><xsl:text>
									</xsl:text><!-- job block --><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><!-- job block --><xsl:text>
									</xsl:text><!-- job block --><xsl:text>
									</xsl:text><xsl:text>
									</xsl:text><!-- job block --><xsl:text>
								</xsl:text></xsl:element><xsl:text>
						</xsl:text></xsl:element></xsl:for-each><xsl:text>
							</xsl:text><!-- experience Type --><xsl:text>
							</xsl:text><!-- experience Type --><xsl:text>
							</xsl:text><xsl:text>
							</xsl:text><!-- experience Type --><xsl:text>
						</xsl:text></xsl:element><xsl:text>
						</xsl:text><!-- Work History --><xsl:text>
						</xsl:text><!-- Education History --><xsl:text>
						</xsl:text><xsl:if test="result/EducationHistory"><xsl:element name="div"><xsl:attribute name="id"><xsl:text>resumeEducationExperience</xsl:text></xsl:attribute><xsl:text>
							</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeSectionTitle</xsl:attribute><xsl:text>Education</xsl:text></xsl:element><xsl:text>
							</xsl:text><!-- education block --><xsl:text>
								</xsl:text><xsl:for-each select="result/EducationHistory/*"><xsl:variable name="DexterDepthLevel1"><xsl:value-of select="position()"/></xsl:variable><xsl:element name="div"><xsl:text>
									</xsl:text><xsl:element name="div"><xsl:text>
										</xsl:text><xsl:element name="div"><xsl:attribute name="class">company floatleft</xsl:attribute><xsl:value-of select="schoolName"/></xsl:element><xsl:text>
										</xsl:text><xsl:element name="div"><xsl:attribute name="class">location floatleft</xsl:attribute><xsl:value-of select="location"/></xsl:element><xsl:text>
										</xsl:text><xsl:element name="div"><xsl:attribute name="class">location floatleft</xsl:attribute><xsl:value-of select="duration"/></xsl:element><xsl:text>
										</xsl:text><xsl:element name="div"><xsl:attribute name="class">clearboth</xsl:attribute></xsl:element><xsl:text>
									</xsl:text></xsl:element><xsl:text>
									</xsl:text><xsl:if test="notes"><xsl:element name="div"><xsl:text>
										</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeSchoolMajor</xsl:attribute><xsl:text>
											</xsl:text><xsl:element name="ul"><xsl:text>
												</xsl:text><xsl:for-each select="notes/*"><xsl:variable name="DexterDepthLevel2"><xsl:value-of select="position()"/></xsl:variable><xsl:element name="li"><xsl:value-of select="note"/></xsl:element></xsl:for-each><xsl:text>
											</xsl:text></xsl:element><xsl:text>
										</xsl:text></xsl:element><xsl:text>
									</xsl:text></xsl:element></xsl:if><xsl:text>
								</xsl:text></xsl:element></xsl:for-each><xsl:text>
							</xsl:text><!-- education block --><xsl:text>
						</xsl:text></xsl:element></xsl:if><xsl:text>
						</xsl:text><!-- Education History --><xsl:text>
						</xsl:text><!-- Volunteer History --><xsl:text>
						</xsl:text><xsl:if test="result/VolunteerHistory"><xsl:element name="div"><xsl:attribute name="id"><xsl:text>resumeVolunteerExperience</xsl:text></xsl:attribute><xsl:text>
							</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeSectionTitle</xsl:attribute><xsl:text>Community Leadership</xsl:text></xsl:element><xsl:text>
							</xsl:text><!-- volunteer block --><xsl:text>
							</xsl:text><xsl:for-each select="result/VolunteerHistory/*"><xsl:variable name="DexterDepthLevel1"><xsl:value-of select="position()"/></xsl:variable><xsl:element name="div"><xsl:text>
								</xsl:text><xsl:element name="div"><xsl:text>
									</xsl:text><xsl:element name="div"><xsl:attribute name="class">company floatleft</xsl:attribute><xsl:value-of select="company"/></xsl:element><xsl:text>
									</xsl:text><xsl:element name="div"><xsl:attribute name="class">location floatleft</xsl:attribute><xsl:value-of select="location"/></xsl:element><xsl:text>
									</xsl:text><xsl:element name="div"><xsl:attribute name="class">duration floatleft</xsl:attribute><xsl:value-of select="duration"/></xsl:element><xsl:text>
									</xsl:text><xsl:element name="div"><xsl:attribute name="class">clearboth</xsl:attribute></xsl:element><xsl:text>
								</xsl:text></xsl:element><xsl:text>
								</xsl:text><xsl:if test="notes"><xsl:element name="div"><xsl:text>
									</xsl:text><xsl:element name="div"><xsl:attribute name="class">resumeSchoolMajor</xsl:attribute><xsl:text>
										</xsl:text><xsl:element name="ul"><xsl:text>
											</xsl:text><xsl:for-each select="notes/*"><xsl:variable name="DexterDepthLevel2"><xsl:value-of select="position()"/></xsl:variable><xsl:element name="li"><xsl:value-of select="note"/></xsl:element></xsl:for-each><xsl:text>
										</xsl:text></xsl:element><xsl:text>
									</xsl:text></xsl:element><xsl:text>
								</xsl:text></xsl:element></xsl:if><xsl:text>
							</xsl:text></xsl:element></xsl:for-each><xsl:text>
							</xsl:text><!-- volunteer block --><xsl:text>
							</xsl:text><!-- volunteer block --><xsl:text>
							</xsl:text><xsl:text>
							</xsl:text><!-- volunteer block --><xsl:text>
						</xsl:text></xsl:element></xsl:if><xsl:text>
						</xsl:text><!-- Volunteer History --><xsl:text>
					</xsl:text></xsl:element></xsl:template></xsl:stylesheet>