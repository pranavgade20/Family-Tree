/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package pranavgade20.com.familytree.gedcom4j.parser;

import java.util.List;

import pranavgade20.com.familytree.gedcom4j.model.AbstractCitation;
import pranavgade20.com.familytree.gedcom4j.model.ChangeDate;
import pranavgade20.com.familytree.gedcom4j.model.CustomFact;
import pranavgade20.com.familytree.gedcom4j.model.HasCustomFacts;
import pranavgade20.com.familytree.gedcom4j.model.NoteStructure;
import pranavgade20.com.familytree.gedcom4j.model.Place;
import pranavgade20.com.familytree.gedcom4j.model.StringTree;
import pranavgade20.com.familytree.gedcom4j.model.StringWithCustomFacts;

/**
 * A Parser for custom tags. Loads custom tags into object that have a collection of {@link CustomFact} objects (i.e., implement the
 * {@link HasCustomFacts} interface).
 * 
 * @author frizbog
 */
public class CustomFactParser extends AbstractParser<CustomFact> {

    /**
     * Constructor
     * 
     * @param gedcomParser
     *            a reference to the root {@link GedcomParser}
     * @param stringTree
     *            {@link StringTree} to be parsed
     * @param loadInto
     *            the object we are loading data into
     */
    CustomFactParser(GedcomParser gedcomParser, StringTree stringTree, CustomFact loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void parse() {
        loadInto.setXref(stringTree.getXref());
        loadInto.setDescription(stringTree.getValue());
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.TYPE.equalsText(ch.getTag())) {
                    loadInto.setType(parseStringWithCustomFacts(ch));
                } else if (Tag.CHANGED_DATETIME.equalsText(ch.getTag())) {
                    ChangeDate changeDate = new ChangeDate();
                    loadInto.setChangeDate(changeDate);
                    new ChangeDateParser(gedcomParser, ch, changeDate).parse();
                } else if (Tag.DATE.equalsText(ch.getTag())) {
                    loadInto.setDate(ch.getValue());
                } else if (Tag.PLACE.equalsText(ch.getTag())) {
                    Place place = new Place();
                    loadInto.setPlace(place);
                    new PlaceParser(gedcomParser, ch, place).parse();
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<NoteStructure> notes = loadInto.getNoteStructures(true);
                    new NoteStructureListParser(gedcomParser, ch, notes).parse();
                } else if (Tag.SOURCE.equalsText(ch.getTag())) {
                    List<AbstractCitation> citations = loadInto.getCitations(true);
                    new CitationListParser(gedcomParser, ch, citations).parse();
                } else if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    if (loadInto.getDescription() == null) {
                        loadInto.setDescription(parseStringWithCustomFacts(ch));
                    } else {
                        loadInto.getDescription().setValue(loadInto.getDescription().getValue() + ch.getValue());
                    }
                } else if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    if (loadInto.getDescription() == null) {
                        loadInto.setDescription(new StringWithCustomFacts(ch.getValue() == null ? "" : ch.getValue()));
                    } else {
                        loadInto.getDescription().setValue(loadInto.getDescription().getValue() + "\n" + ch.getValue());
                    }
                } else {
                    unknownTag(ch, loadInto);
                }
            }
        }
    }

}
