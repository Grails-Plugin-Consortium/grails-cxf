package org.grails.cxf.adapter

import javax.xml.bind.annotation.adapters.XmlAdapter

/**
 * A class that can be used directly or as a template to do map transformations
 * of service method responses.  This will currently only marshal to<->from
 * primitives.  If you want to use objects (and not use the simple .toString()
 * as I have done) you will need to roll your own adapter to marshal and
 * unmarshal your data.  For example if you are using domain objects whose
 * toString() returns more than just an id.
 *
 * @code
 * \@XmlJavaTypeAdapter(GrailsCxfMapAdapter.class)
 * Map<String, String> returnMap() {
 *      return ['foo': ''bar', 'ying': 'yang']
 *}
 */
class GrailsCxfMapAdapter extends XmlAdapter<GrailsCxfMap, Map> {

    @Override Map unmarshal(GrailsCxfMap v) throws Exception {
        Map map = new LinkedHashMap()
        v.entries.each { e ->
            map.put(e.key, e.value)
        }
        map
    }

    @Override GrailsCxfMap marshal(Map v) throws Exception {
        GrailsCxfMap map = new GrailsCxfMap()
        for(Map.Entry e : v.entrySet()) {
            GrailsCxfMap.KeyValueEntry kve = new GrailsCxfMap.KeyValueEntry()
            kve.key = e.key.toString()
            kve.value = e.value.toString()
            map.entries << kve
        }
        map
    }
}
