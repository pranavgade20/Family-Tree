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

import pranavgade20.com.familytree.gedcom4j.exception.UnsupportedVersionException;
import pranavgade20.com.familytree.gedcom4j.model.GedcomVersion;
import pranavgade20.com.familytree.gedcom4j.model.StringTree;
import pranavgade20.com.familytree.gedcom4j.model.enumerations.SupportedVersion;

/**
 * A parser for {@link GedcomVersion} objects.
 * 
 * @author frizbog
 */
class GedcomVersionParser extends AbstractParser<GedcomVersion> {

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
    GedcomVersionParser(GedcomParser gedcomParser, StringTree stringTree, GedcomVersion loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void parse() {
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.VERSION.equalsText(ch.getTag())) {
                    SupportedVersion vn = null;
                    try {
                        vn = SupportedVersion.forString(ch.getValue());
                    } catch (UnsupportedVersionException e) {
                        addError(e.getMessage());
                    }
                    loadInto.setVersionNumber(vn);
                    remainingChildrenAreCustomTags(ch, loadInto.getVersionNumber());
                } else if (Tag.FORM.equalsText(ch.getTag())) {
                    loadInto.setGedcomForm(parseStringWithCustomFacts(ch));
                } else {
                    unknownTag(ch, loadInto);
                }
            }
        }
    }

}
