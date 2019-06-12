package pl.put.poznan.oculus.oculusimagesservice.rest.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "details of image file")
data class ImageFileCreateRequest (
        @ApiModelProperty(notes = "id of patient")
        val patient: String,
        @ApiModelProperty(notes = "id of doctor")
        val author: String,
        @ApiModelProperty(notes = "notes")
        val notes: String
)