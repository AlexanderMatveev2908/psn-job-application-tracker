import { FaBuilding } from "react-icons/fa6";
import { MdOutlineUpdate, MdWork } from "react-icons/md";
import { JobApplicationT } from "@/features/jobApplications/types";
import { FaPenFancy, FaRegTrashAlt } from "react-icons/fa";
import { IoIosCreate } from "react-icons/io";
import { formatDate } from "@/core/lib/dataStructure/formatters";

export const genPairsMainCardInfo = (jobAppl: JobApplicationT) =>
  [
    {
      key: "companyName",
      label: "Company Name",
      Svg: FaBuilding,
    },
    {
      key: "positionName",
      label: "Position Name",
      Svg: MdWork,
    },
    {
      key: "appliedAt",
      label: "Applied At",
      Svg: FaPenFancy,
    },
  ].map((el) => {
    const v = jobAppl[el.key as keyof JobApplicationT];

    return {
      ...el,
      val: el.key !== "appliedAt" ? v : formatDate(v as number),
    };
  });

export const genPairsSecondaryInfoCard = (jobAppl: JobApplicationT) =>
  [
    {
      key: "created_at",
      Svg: IoIosCreate,
    },
    {
      key: "updated_at",
      Svg: MdOutlineUpdate,
    },
  ].map((el) => ({
    ...el,
    val: formatDate(jobAppl[el.key as keyof JobApplicationT] as number),
  }));

export const btnsFooter = [
  {
    label: "Update",
    Svg: MdOutlineUpdate,
  },
  {
    label: "Delete",
    Svg: FaRegTrashAlt,
  },
];
