/*******************************************************************************
 * Copyright (c) 2015, Edoardo Fusella
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *       * Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *       * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in
 *         the documentation and/or other materials provided with the
 *         distribution.
 *       * Neither the name of the University of Naples Federico II nor the names of its
 *         contributors may be used to endorse or promote products derived
 *         from this software without specific prior written permission.
 *   
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 *   IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 *   TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *   PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *   HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 *   TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Provides the output file writer. An object of this class is 
 * required to create an output fie containing the results of
 * the optimization process. The file will be located in the output/ folder.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 *
 */
public class OutputFileWriter {
	/**
	 * Constructs an OutputFileWriter object required to 
	 * write the output of the mapping optimization phase on a file.
	 * 
	 * @param file_name The output file name
	 */
	public OutputFileWriter(String file_name){
		this.file_name = "output/"+file_name;
		BufferedWriter writer = null;
		File logFile = new File(this.file_name);
		try {
			writer = new BufferedWriter(new FileWriter(logFile));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Writes a formatted representations of the application/architecture
	 * setup and the output of the mapping optimization phase on the output file.
	 * matrix to a text-output stream. 
	 * 
	 * @param text The data that must be written on the output file.
	 */
	public void write(String text){
		BufferedWriter writer = null;
        try {
            File logFile = new File(this.file_name);
            writer = new BufferedWriter(new FileWriter(logFile, true));
            // This will output the full path where the file will be written to...
            //System.out.println(logFile.getCanonicalPath());
            writer.write(text);
           
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
	}
	
	/**
	 * The output file name
	 */
	private String file_name;
}
