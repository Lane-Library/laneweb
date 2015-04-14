package edu.stanford.irt.laneweb.bassett;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * @author alainb
 */
public class BassettAccordionGenerator extends AbstractGenerator implements ModelAware {

    private static final String[][] TOTALS = { { "Abdomen", "140" }, { "Abdomen--Adrenal Gland", "29" },
            { "Abdomen--Bones Cartilage Joints", "4" }, { "Abdomen--Central Nervous System", "2" },
            { "Abdomen--Fascia", "16" }, { "Abdomen--Gallbladder", "27" }, { "Abdomen--Kidney", "29" },
            { "Abdomen--Large Intestine", "14" }, { "Abdomen--Liver", "29" }, { "Abdomen--Lymphatics", "12" },
            { "Abdomen--Muscles And Tendons", "55" }, { "Abdomen--Overview", "33" }, { "Abdomen--Pancreas", "27" },
            { "Abdomen--Peripheral Nervous System", "56" }, { "Abdomen--Small Intestine", "11" },
            { "Abdomen--Spleen", "29" }, { "Abdomen--Stomach", "15" }, { "Abdomen--Vasculature", "70" },
            { "Back", "110" }, { "Back--Bones Joints Cartilage", "34" }, { "Back--Central Nervous System", "22" },
            { "Back--Cervical Region", "42" }, { "Back--Lumbar Region", "52" }, { "Back--Meninges", "5" },
            { "Back--Muscles And Tendons", "40" }, { "Back--Overview", "5" },
            { "Back--Peripheral Nervous System", "6" }, { "Back--Sacral Region", "37" },
            { "Back--Thoracic Region", "55" }, { "Back--Vasculature", "17" }, { "Back--Vertebral Column", "107" },
            { "Female Pelvis", "87" }, { "Female Pelvis--Anal Canal", "6" },
            { "Female Pelvis--Bones Joints Cartilage", "26" }, { "Female Pelvis--Central Nervous System", "8" },
            { "Female Pelvis--External Genitalia", "2" }, { "Female Pelvis--Large Intestine", "1" },
            { "Female Pelvis--Muscles And Tendons", "16" }, { "Female Pelvis--Ovary", "8" },
            { "Female Pelvis--Overview", "1" }, { "Female Pelvis--Perineum", "10" },
            { "Female Pelvis--Peripheral Nervous System", "18" }, { "Female Pelvis--Urinary Tract", "6" },
            { "Female Pelvis--Uterus", "12" }, { "Female Pelvis--Vagina", "14" },
            { "Female Pelvis--Vasculature", "16" }, { "Head", "493" }, { "Head--Bones Cartilage Joints", "176" },
            { "Head--Brain", "225" }, { "Head--Cerebellum", "51" }, { "Head--Cheek", "41" },
            { "Head--Connective Tissue", "49" }, { "Head--Diencephalon", "71" }, { "Head--Ear", "37" },
            { "Head--Exocrine And Endocrine", "15" }, { "Head--Eye", "62" }, { "Head--Face", "153" },
            { "Head--Frontal Lobe", "22" }, { "Head--Medulla", "32" }, { "Head--Meninges", "23" },
            { "Head--Midbrain", "54" }, { "Head--Mouth", "63" }, { "Head--Muscles And Tendons", "70" },
            { "Head--Nose", "20" }, { "Head--Occipital Lobe", "21" }, { "Head--Overview", "49" },
            { "Head--Parietal Lobe", "20" }, { "Head--Peripheral Nervous System", "124" }, { "Head--Pons", "37" },
            { "Head--Scalp", "16" }, { "Head--Telencephalon", "134" }, { "Head--Temporal Lobe", "28" },
            { "Head--Vasculature", "131" }, { "Head--Ventricules", "61" }, { "Lower Extremity", "217" },
            { "Lower Extremity--Ankle", "42" }, { "Lower Extremity--Bones Joints Cartilage", "75" },
            { "Lower Extremity--Fascia", "19" }, { "Lower Extremity--Foot And Toes", "78" },
            { "Lower Extremity--Knee", "22" }, { "Lower Extremity--Leg", "41" },
            { "Lower Extremity--Muscles And Tendons", "152" }, { "Lower Extremity--Peripheral Nervous System", "80" },
            { "Lower Extremity--Thigh", "58" }, { "Lower Extremity--Vasculature", "53" }, { "Male Pelvis", "101" },
            { "Male Pelvis--Anal Canal", "2" }, { "Male Pelvis--Bones Joints Cartilage", "33" },
            { "Male Pelvis--Central Nervous System", "10" }, { "Male Pelvis--Large Intestine", "2" },
            { "Male Pelvis--Muscles And Tendons", "35" }, { "Male Pelvis--Perineum", "3" },
            { "Male Pelvis--Peripheral Nervous System", "14" }, { "Male Pelvis--Prostate", "2" },
            { "Male Pelvis--Urinary Tract", "6" }, { "Male Pelvis--Vasculature", "12" }, { "Neck", "130" },
            { "Neck--Bones Cartilage Joints", "32" }, { "Neck--Central Nervous System", "9" },
            { "Neck--Cervical Vertebrae", "23" }, { "Neck--Esophagus", "5" }, { "Neck--Exocrine And Endocrine", "18" },
            { "Neck--Fascia And Connective Tissue", "37" }, { "Neck--Lymphatics", "9" }, { "Neck--Meninges", "5" },
            { "Neck--Muscles And Tendons", "53" }, { "Neck--Overview", "13" },
            { "Neck--Peripheral Nervous System", "54" }, { "Neck--Pharynx", "17" }, { "Neck--Throat", "51" },
            { "Neck--Trachea", "2" }, { "Neck--Vasculature", "47" }, { "Pelvis", "91" }, { "Pelvis--Anal Canal", "3" },
            { "Pelvis--Bones Joints Cartilage", "15" }, { "Pelvis--Central Nervous System", "17" },
            { "Pelvis--External Genitalia", "8" }, { "Pelvis--Female", "1" }, { "Pelvis--Large Intestine", "6" },
            { "Pelvis--Muscles And Tendons", "51" }, { "Pelvis--Ovary", "10" }, { "Pelvis--Overview", "2" },
            { "Pelvis--Perineum", "1" }, { "Pelvis--Peripheral Nervous System", "16" },
            { "Pelvis--Urinary Tract", "8" }, { "Pelvis--Uterus", "8" }, { "Pelvis--Vagina", "2" },
            { "Pelvis--Vasculature", "32" }, { "Thorax", "147" }, { "Thorax--Bones Joints Cartilage", "33" },
            { "Thorax--Breast", "7" }, { "Thorax--Central Nervous System", "6" }, { "Thorax--Diaphragm", "8" },
            { "Thorax--Esophagus", "10" }, { "Thorax--Fascia And Connective Tissue", "12" }, { "Thorax--Heart", "46" },
            { "Thorax--Left Heart", "33" }, { "Thorax--Left Lung", "21" }, { "Thorax--Lung", "39" },
            { "Thorax--Lymphatics", "9" }, { "Thorax--Mediastinum", "23" }, { "Thorax--Muscles And Tendons", "28" },
            { "Thorax--Overview", "7" }, { "Thorax--Pericardial Sac", "25" },
            { "Thorax--Peripheral Nervous System", "32" }, { "Thorax--Pleura", "11" }, { "Thorax--Rib Cage", "16" },
            { "Thorax--Right Heart", "31" }, { "Thorax--Right Lung", "17" }, { "Thorax--Skin", "2" },
            { "Thorax--Thymus", "2" }, { "Thorax--Vasculature", "43" }, { "Thorax--Vertebral Column", "20" },
            { "Upper Extremity", "196" }, { "Upper Extremity--Axilla", "13" }, { "Upper Extremity--Elbow", "19" },
            { "Upper Extremity--Fascia Ligaments And Tendons", "64" }, { "Upper Extremity--Forearm", "45" },
            { "Upper Extremity--Hand And Fingers", "70" }, { "Upper Extremity--Lymphatics", "2" },
            { "Upper Extremity--Muscles And Tendons", "65" }, { "Upper Extremity--Neuralnetwork", "71" },
            { "Upper Extremity--Overview", "10" }, { "Upper Extremity--Pectoral Region", "15" },
            { "Upper Extremity--Peripheral Nervous System", "46" }, { "Upper Extremity--Shoulder", "33" },
            { "Upper Extremity--Skin", "7" }, { "Upper Extremity--Upper Arm", "19" },
            { "Upper Extremity--Vasculature", "105" }, { "Upper Extremity--Wrist", "8" } };

    private static final Map<String, Integer> TOTALS_MAP;
    static {
        Map<String, Integer> totals = new LinkedHashMap<String, Integer>();
        for (String[] element : TOTALS) {
            totals.put(element[0], Integer.parseInt(element[1]));
        }
        TOTALS_MAP = Collections.unmodifiableMap(totals);
    }

    private BassettCollectionManager collectionManager;

    private String query;

    private SAXStrategy<Map<String, Integer>> saxStrategy;

    public BassettAccordionGenerator(final BassettCollectionManager collectionManager,
            final SAXStrategy<Map<String, Integer>> saxStrategy) {
        this.collectionManager = collectionManager;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Map<String, Integer> counts = this.query == null || this.query.isEmpty() ? TOTALS_MAP : this.collectionManager
                .searchCount(this.query);
        this.saxStrategy.toSAX(counts, xmlConsumer);
    }
}
