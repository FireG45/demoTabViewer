import React, { useRef, useEffect } from 'react'
import VexFlow from 'vexflow'
const { Renderer, TabStave, TabNote, Formatter} = VexFlow.Flow

export default function Stave({ measures = null }) {
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
    
    renderer.resize(500, 300);
    const context = renderer.getContext();

    const stave = new TabStave(10, 40, 400);
    stave.addClef("tab").setContext(context).draw();

    let notes = [];

    let measure = measures[0].noteDTOS;

    for (let i = 0; i < measure.length; i++) {
      let note = new TabNote({
        positions: [{ str: measure[0].string, fret: measure[0].fret }],
        duration: "q",
      })
      notes.push(note)
    }


    if (notes.length !== 0) {
      Formatter.FormatAndDraw(context, stave, notes);
    }
  })
  return <div ref={container} />
}