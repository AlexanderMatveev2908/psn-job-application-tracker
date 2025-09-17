import { NextRequest, NextResponse } from "next/server";

const shutBrowser = async (req: NextRequest) => {
  if (req.nextUrl.pathname.endsWith(".js.map"))
    return new Response(null, { status: 204 });

  return NextResponse.next();
};

export default shutBrowser;
