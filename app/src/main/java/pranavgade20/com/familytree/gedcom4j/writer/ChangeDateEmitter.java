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
package pranavgade20.com.familytree.gedcom4j.writer;

import pranavgade20.com.familytree.gedcom4j.exception.GedcomWriterException;
import pranavgade20.com.familytree.gedcom4j.exception.WriterCancelledException;
import pranavgade20.com.familytree.gedcom4j.model.ChangeDate;

/**
 * @author frizbog
 *
 */
class ChangeDateEmitter extends AbstractEmitter<ChangeDate> {
    /**
     * Constructor
     * 
     * @param baseWriter
     *            The base Gedcom writer class this Emitter is partnering with to emit the entire file
     * @param startLevel
     *            write starting at this level
     * @param writeFrom
     *            object to write
     * @throws WriterCancelledException
     *             if cancellation was requested during the operation
     */
    ChangeDateEmitter(GedcomWriter baseWriter, int startLevel, ChangeDate writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void emit() throws GedcomWriterException {
        if (writeFrom != null) {
            emitTag(startLevel, "CHAN");
            emitTagWithRequiredValue(startLevel + 1, "DATE", writeFrom.getDate());
            emitTagIfValueNotNull(startLevel + 2, "TIME", writeFrom.getTime());
            new NoteStructureEmitter(baseWriter, startLevel + 1, writeFrom.getNoteStructures()).emit();
            emitCustomFacts(startLevel + 1, writeFrom.getCustomFacts());
        }
    }

}
