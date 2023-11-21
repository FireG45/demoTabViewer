import { Button } from "@mui/material";
import React, { useState } from "react";

const SingleFileUploader = () => {
  const [file, setFile] = useState(null);

  const handleFileChange = (e) => {
    if (e.target.files) {
      setFile(e.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (file) {
      console.log("Uploading file...");
  
      const formData = new FormData();
      formData.append("file", file);
  
      try {
        const result = await fetch("http://localhost:8080/upload", {
          method: "POST",
          body: formData,
        });
  
        const data = await result;
  
        console.log(data);
      } catch (error) {
        console.error(error);
      }
    }
  };

  return (
    <>
      <div>
        <input id="file" type="file" onChange={handleFileChange} />
      </div>
      {file && <Button onClick={handleUpload}>Upload a file</Button>}
    </>
  );
};

export default SingleFileUploader;