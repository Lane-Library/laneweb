package edu.stanford.irt.laneweb.ldap;

/*
 * Created on Mar 14, 2007 Created by Philip S. Constantinou
 */
import java.util.HashMap;
import java.util.Map;

/**
 * Created on: Mar 14, 2007 Affiliation
 * 
 * @see <a href=
 *      "https://www.stanford.edu/dept/as/mais/integration/data/data_affiliation_types.html"
 *      >LDAP
 * @see group for lane
 *      https://medwiki.stanford.edu/display/lane/Proxy+server+documentation
 *      Union between stanford:stanford,stanford:academic, stanford:admin, unc
 *      Data Affiliation types</a>
 * @author philip constantinou
 */
public enum Affiliation {
    AffiliateSponsored("stanford:affiliate:sponsored", "Affiliate (Sponsored)"),
    Affiliate("stanford:affiliate*", "All Stanford Affiliates", false), 
    AffiliateFellow("stanford:affiliate:fellow", "Affiliate (Fellow)", false),
    AffiliateCourtesy("stanford:affiliate:courtesy", "Affiliate (Courtesy)", false), 
    AffiliateNonactive("stanford:affiliate:nonactive", "Affiliate (Nonactive)", false),
    AffiliateVisitingScholarLongTerm("stanford:affiliate:visitscholarvt", "Affiliate (Visiting Scholar Long Term)", false),
    AffiliateVisitingScholarShortTerm("stanford:affiliate:visitscholarvs", "Affiliate (Visiting Scholar Short Term)", false),
    
    Faculty("stanford:faculty*", "All Faculty"), 
    FacultyOnLeave("stanford:faculty:onleave",   "Faculty (On leave)"), 
    FacultyOtherTeaching( "stanford:faculty:otherteaching", "Faculty (Other teaching)"), 
    FacultySLAC( "stanford:faculty:slac", "Faculty (SLAC)"), 
    FacultyActive("stanford:faculty", "Faculty (Active)"), 
    FacultyAffiliate("stanford:faculty:affiliate","Faculty affiliate"), 
    FacultyEmeritus("stanford:faculty:emeritus", "Emeritus Faculty"),
    FacultyRetired("stanford:faculty:retired", "Retired Faculty"),
    FacultyNonactive("stanford:faculty:nonactive", "Faculty (Nonactive)", false), 
    
    Staff("stanford:staff*", "All staff"), 
    StaffActive("stanford:staff", "Staff (Active)"), 
    StaffAcademic("stanford:staff:academic", "Staff (Academic)"), 
    StaffOnLeave("stanford:faculty:onleave",   "Staff (On leave)"),
    StaffEmeritus("stanford:staff:emeritus", "Staff (Emeritus)"),
    StaffOtherteaching("stanford:staff:otherteaching", "Staff (Otherteaching)"), 
    StaffTemporary("stanford:staff:temporary", "Staff (Temporary"), 
    StaffCasual("stanford:staff:casual", "Staff (Casual)"), 
    StaffRetired("stanford:staff:retired", "Staff (Retired)", false), 
    StaffAffiliate("stanford:faculty:affiliate","Staff affiliate", false),
    StaffStudent("stanford:staff:student", "Staff (Student)", false), 
    StaffNonactive("stanford:staff:nonactive", "Staff (Nonactive)", false), 
    
    
    Student("stanford:student*", "All students"), 
    StudentActive("stanford:student", "Student (Active)"),
    StudentOnLeave("stanford:student:onleave", "Student (On leave)"), 
    StudentPostDoc("stanford:student:postdoc", "Student (Postdoc)"), 
    StudentMLA("stanford:student:mla", "Student (MLA)"), 
    StudentNDO("stanford:student:ndo", "Student (NDO)"), 
    
    StudentIncoming("stanford:student:incoming", "Sudent (Incoming)", false), 
    StudentContingent("stanford:student:contingent", "Student (Contingent)", false), 
    StudentNotRegistered("stanford:student:notregistered", "Student (Not registered)", false), 
    StudentRecent("stanford:student:recent", "Student (Recent)", false), 
    StudentNonactive("stanford:student:nonactive", "Student (Nonactive)", false), 
    
    SUMCStaff("sumc:staff", "SUMC Staff"), 
    SUMCStaffNonactive("sumc:staff:nonactive", "SUMC Staff (Nonactive)", false),
    
    
    AffiliationNotFound("Affiliation not found", "Affiliate Not Found", false);

    /*
     * Removed per AS documentation: FacultyRetired( "stanford:faculty:retired",
     * "Faculty (Retired)", false)
     */
    private static Map<String, Affiliation> sAffiliations = null;
    
    static {
        sAffiliations = new HashMap<String, Affiliation>();
        for (Affiliation a : values()) {
            sAffiliations.put(a.ldapAffiliateString, a);
        }
    }

    public static final Affiliation getAffiliation(final String ldapString) {
        Affiliation affiliation = sAffiliations.get(ldapString);
        if (null != affiliation) {
            return affiliation;
        } else {
            return AffiliationNotFound;
        }
    }

    private boolean active;

    private String display;

    private String ldapAffiliateString;

    Affiliation(final String ldapAffiliateString, final String display) {
        this(ldapAffiliateString, display, true);
    }

    Affiliation(final String ldapAffiliateString, final String display, final boolean active) {
        this.ldapAffiliateString = ldapAffiliateString;
        this.display = display;
        this.active = active;
    }

    public String getDisplay() {
        return this.display;
    }

    /**
     * @return the ldapAffiliateString
     */
    public String getLdapAffiliateString() {
        return this.ldapAffiliateString;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return this.active;
    }
}