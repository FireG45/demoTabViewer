import React, { useRef, useEffect } from 'react'
import VexFlow from 'vexflow'
import parseEffects from './renderUtils/parseEffects'
import drawHummerSlide from './renderUtils/drawHummerSlide'
import drawBeams from './renderUtils/drawBeams'
import drawPalmMutes from './renderUtils/drawPalmMutes'
import drawLetRing from './renderUtils/drawLetRing'
const { Renderer, TabStave, TabNote, Formatter, StaveNote } = VexFlow.Flow

export default function Stave({ measure = null, stringCount = 6, tempo = 0, timeSignature = "", tuning = "", staveId = 0, pmIndexes = null , slidesAndTies = null, lrIndexes = null}) {
  const container = useRef()
  const rendererRef = useRef()

  useEffect(() => {
    if (rendererRef.current == null) {
      rendererRef.current = new Renderer(
        container.current,
        Renderer.Backends.SVG
      )
    }
    const renderer = rendererRef.current
    
    renderer.resize(500, 160 + (stringCount % 6 * 15));
    const context = renderer.getContext();

    let shift = tuning ? 20 : 0
    const stave = new TabStave(shift, 0, 448 - shift);
    stave.options.num_lines = stringCount;
    stave.options.line_config = new Array(stringCount).fill({visible : true});
    
    if (tempo) {
      const bpm = tempo;
      const x = measure[0] && measure[0].effects[0] === 't' ? -12 : 0;
      const y = 0;
      context.setFont("Arial", 20);
      context.fillText("♩" , y + 5, x + 25);
      context.setFont("Arial", 12);
      context.fillText(bpm, y, x + 40);
    }

    if (staveId) {
      const id = staveId;
      
      context.setFont("Arial", 8);
      context.fillText(id, shift, 50);
    }

    if (tuning) {
      context.setFont("Arial", 10);
      for (let i = 1; i < tuning.length; i++) {
        const element = tuning[i - 1];
        context.fillText(element , 0, i * 13 + 43);
      }
    }

    if (timeSignature) {
      stave.addTimeSignature(timeSignature);
    }

    stave.setContext(context).draw()
    
    let notes = [];
    let beamNotes = [];
    for (let i = 0; i < measure.length; i++) {
      let beat = measure[i].noteDTOS;
      let duration = measure[i].duration;
      let effects = measure[i].effects;
      let ghostNote = measure[i].ghostNote;
      var pos = []

      for (let j = 0; j < beat.length; j++) {
        pos.push({ str: beat[j].string, fret: beat[j].fret })
      }

      if (pos.length > 0) {
        var note = new TabNote({positions: pos, duration: duration});
        note.setGhost(ghostNote)
        let parsedEffects = parseEffects(effects);
        
        for (let i = 0; i < parsedEffects.length; i++) {
          const element = parsedEffects[i];
          note.addModifier(element)
        }

        notes.push(note)
        beamNotes.push(note)
      } else {
        notes.push(new StaveNote({ keys: ["b/4"], duration: duration + "r"}))
      }
    }

    if (notes && notes.length > 0) Formatter.FormatAndDraw(context, stave, notes);

    drawPalmMutes(pmIndexes, notes, context, shift)
    drawLetRing(lrIndexes, notes, context, shift)

    drawBeams(notes, context)

    drawHummerSlide(slidesAndTies, notes, context)
  })

  return <div ref={container} />
}