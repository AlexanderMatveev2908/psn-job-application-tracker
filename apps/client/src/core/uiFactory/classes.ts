import { FormFieldCheckT, FormFieldTxtT } from "@/common/types/ui";
import { FieldValues, Path } from "react-hook-form";
import { v4 } from "uuid";
import { captAll } from "../lib/dataStructure/formatters";

export class FormFieldGen<T extends FieldValues> {
  private genID(): string {
    return v4();
  }

  private extractLabel(name: Path<T>, label?: string | null) {
    return label ?? captAll(name!.replace(/_/g, " "));
  }

  public txtField(
    arg: Partial<FormFieldTxtT<T>>
  ): NonNullable<FormFieldTxtT<T>> & { id: string } {
    const label = this.extractLabel(arg.name!, arg.label);

    return {
      id: this.genID(),
      name: arg.name!,
      label,
      place: (arg.place ?? label) + "...",
      type: arg.type ?? "text",
    };
  }

  public checkField(
    arg: Partial<FormFieldCheckT<T>>
  ): NonNullable<FormFieldCheckT<T>> & { id: string } {
    return {
      id: this.genID(),
      name: arg.name!,
      label: this.extractLabel(arg.name!, arg.label),
      type: arg.type ?? "checkbox",
    };
  }
}
