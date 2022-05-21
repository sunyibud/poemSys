package com.poemSys.admin.bean.Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdsForm
{
    private static final long serialVersionUID = 1L;

    List<IdForm> ids;
}
